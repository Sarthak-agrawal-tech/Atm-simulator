$jars = Get-ChildItem lib -Filter *.jar | ForEach-Object { $_.FullName }
$compileClasspath = ($jars + ".") -join ";"
$runClasspath = @("bin") + $jars -join ";"
$compileLog = "bin\javac-whitebox-stderr.log"

New-Item -ItemType Directory -Force bin | Out-Null
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

java -cp $runClasspath test.WhiteBoxTestRunner
exit $LASTEXITCODE
