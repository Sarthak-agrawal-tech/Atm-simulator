$jars = Get-ChildItem lib -Filter *.jar | ForEach-Object { $_.FullName }
$compileClasspath = ($jars + ".") -join ";"
$runClasspath = @("bin") + $jars -join ";"
$compileLog = "bin\javac-whitebox-stderr.log"
$reportRoot = "test-reports\whitebox"
$jacocoExec = Join-Path $reportRoot "jacoco.exec"
$jacocoCsv = Join-Path $reportRoot "jacoco.csv"
$jacocoXml = Join-Path $reportRoot "jacoco.xml"
$jacocoHtml = Join-Path $reportRoot "jacoco-html"
$resultsCsv = Join-Path $reportRoot "test-results.csv"
$summaryHtml = Join-Path $reportRoot "summary.html"

New-Item -ItemType Directory -Force bin, $reportRoot, $jacocoHtml | Out-Null
if (Test-Path $compileLog) {
    Remove-Item $compileLog -Force
}

$sourceFiles = Get-ChildItem src, test -Recurse -Filter *.java | ForEach-Object { $_.FullName }
javac -cp $compileClasspath -d bin $sourceFiles 2> $compileLog

if ($LASTEXITCODE -ne 0) {
    if (Test-Path $compileLog) {
        Get-Content $compileLog
    }
    exit $LASTEXITCODE
}

java "-javaagent:tools\jacocoagent.jar=destfile=$jacocoExec,append=false" "-Dwhitebox.resultsFile=$resultsCsv" -cp $runClasspath test.WhiteBoxTestRunner
$testExitCode = $LASTEXITCODE

java -jar tools\jacococli.jar report $jacocoExec --classfiles bin --sourcefiles src --html $jacocoHtml --xml $jacocoXml --csv $jacocoCsv | Out-Null
if ($LASTEXITCODE -ne 0) {
    exit $LASTEXITCODE
}

$testResults = Import-Csv $resultsCsv
$coverageRows = Import-Csv $jacocoCsv | Where-Object { $_.PACKAGE -eq "src" }

$passedCount = ($testResults | Where-Object { $_.status -eq "SUCCESSFUL" }).Count
$failedCount = ($testResults | Where-Object { $_.status -eq "FAILED" }).Count
$skippedCount = ($testResults | Where-Object { $_.status -eq "SKIPPED" }).Count
$totalTests = $testResults.Count

$missedLines = [int](($coverageRows | Measure-Object -Property LINE_MISSED -Sum).Sum)
$coveredLines = [int](($coverageRows | Measure-Object -Property LINE_COVERED -Sum).Sum)
$totalLines = $missedLines + $coveredLines
$lineCoverage = if ($totalLines -eq 0) { 0 } else { [math]::Round(($coveredLines / $totalLines) * 100, 2) }

$classRows = foreach ($row in $coverageRows) {
    $classTotal = [int]$row.LINE_MISSED + [int]$row.LINE_COVERED
    $classCoverage = if ($classTotal -eq 0) { 0 } else { [math]::Round(([int]$row.LINE_COVERED / $classTotal) * 100, 2) }
    [PSCustomObject]@{
        ClassName = $row.CLASS
        Covered = [int]$row.LINE_COVERED
        Missed = [int]$row.LINE_MISSED
        Coverage = $classCoverage
        Link = "jacoco-html/src/$($row.CLASS).html"
    }
}

$classTableRows = ($classRows | ForEach-Object {
    "<tr><td><a href='$($_.Link)'>$($_.ClassName)</a></td><td>$($_.Covered)</td><td>$($_.Missed)</td><td>$($_.Coverage)%</td></tr>"
}) -join [Environment]::NewLine

$failedTestsRows = ($testResults | Where-Object { $_.status -eq "FAILED" } | ForEach-Object {
    "<tr><td>$($_.testName)</td><td>$($_.message)</td></tr>"
}) -join [Environment]::NewLine

if ([string]::IsNullOrWhiteSpace($failedTestsRows)) {
    $failedTestsRows = "<tr><td colspan='2'>No failed tests in this run.</td></tr>"
}

$html = @"
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>ATM White-Box Test Report</title>
    <style>
        :root {
            --bg: #f5efe6;
            --card: #fffaf3;
            --ink: #1f2a30;
            --accent: #0f766e;
            --warn: #b45309;
            --danger: #b91c1c;
            --line: #d6c7b3;
        }
        body {
            margin: 0;
            font-family: Georgia, "Times New Roman", serif;
            background: linear-gradient(135deg, #f5efe6 0%, #efe3d0 100%);
            color: var(--ink);
        }
        .wrap {
            max-width: 1000px;
            margin: 0 auto;
            padding: 32px 20px 48px;
        }
        .hero, .card {
            background: var(--card);
            border: 1px solid var(--line);
            border-radius: 18px;
            box-shadow: 0 10px 30px rgba(31, 42, 48, 0.08);
        }
        .hero {
            padding: 28px;
            margin-bottom: 24px;
        }
        .hero h1 {
            margin: 0 0 8px;
            font-size: 34px;
        }
        .hero p {
            margin: 0;
            font-size: 17px;
            line-height: 1.5;
        }
        .metrics {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(180px, 1fr));
            gap: 16px;
            margin: 24px 0;
        }
        .metric {
            padding: 18px;
            border-radius: 14px;
            background: #fff;
            border: 1px solid var(--line);
        }
        .metric strong {
            display: block;
            font-size: 30px;
            margin-bottom: 6px;
        }
        .card {
            padding: 22px;
            margin-bottom: 20px;
        }
        table {
            width: 100%;
            border-collapse: collapse;
        }
        th, td {
            padding: 12px 10px;
            border-bottom: 1px solid var(--line);
            text-align: left;
        }
        th {
            font-size: 14px;
            text-transform: uppercase;
            letter-spacing: 0.06em;
        }
        .note {
            color: #475569;
            font-size: 15px;
            line-height: 1.5;
        }
        .accent { color: var(--accent); }
        .warn { color: var(--warn); }
        .danger { color: var(--danger); }
        a { color: var(--accent); }
    </style>
</head>
<body>
    <div class="wrap">
        <section class="hero">
            <h1>ATM White-Box Test Report</h1>
            <p>This report gives a simple view of how much of the Java code was exercised by the white-box tests. Covered lines were executed by the tests. Rejected lines are the lines that were not executed and should be reviewed if deeper coverage is needed.</p>
            <div class="metrics">
                <div class="metric"><strong class="accent">$totalTests</strong>Total tests run</div>
                <div class="metric"><strong class="accent">$passedCount</strong>Tests passed</div>
                <div class="metric"><strong class="danger">$failedCount</strong>Tests failed</div>
                <div class="metric"><strong class="warn">$skippedCount</strong>Tests skipped</div>
                <div class="metric"><strong class="accent">$lineCoverage%</strong>Overall line coverage</div>
                <div class="metric"><strong class="accent">$coveredLines</strong>Covered lines</div>
                <div class="metric"><strong class="danger">$missedLines</strong>Rejected lines</div>
            </div>
        </section>
        <section class="card">
            <h2>Coverage By Class</h2>
            <p class="note">Open any class name to view the detailed JaCoCo page. In those pages, green lines were covered and red lines were missed.</p>
            <table>
                <thead><tr><th>Class</th><th>Covered Lines</th><th>Rejected Lines</th><th>Coverage</th></tr></thead>
                <tbody>
                    $classTableRows
                </tbody>
            </table>
        </section>
        <section class="card">
            <h2>Failed Tests</h2>
            <table>
                <thead><tr><th>Test</th><th>Reason</th></tr></thead>
                <tbody>
                    $failedTestsRows
                </tbody>
            </table>
        </section>
        <section class="card">
            <h2>Detailed Files</h2>
            <p class="note"><a href="jacoco-html/index.html">Open the full JaCoCo detail report</a> for file-by-file and line-by-line inspection.</p>
        </section>
    </div>
</body>
</html>
"@

Set-Content -Path $summaryHtml -Value $html -Encoding UTF8
Write-Host "White-box summary report: $summaryHtml"
Write-Host "JaCoCo detail report: $(Join-Path $jacocoHtml 'index.html')"
exit $testExitCode
