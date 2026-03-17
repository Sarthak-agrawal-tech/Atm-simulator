$base = "$env:USERPROFILE\.vscode\extensions\vscjava.vscode-java-test-0.44.0\server"
$jars = @(
    "$base\junit-jupiter-api_5.14.1.jar",
    "$base\junit-jupiter-engine_5.14.1.jar",
    "$base\junit-jupiter-params_5.14.1.jar",
    "$base\junit-platform-commons_1.14.1.jar",
    "$base\junit-platform-engine_1.14.1.jar",
    "$base\junit-platform-launcher_1.14.1.jar",
    "$base\org.apiguardian.api_1.1.2.jar",
    "$base\org.opentest4j_1.3.0.jar"
)

$compileClasspath = ($jars + ".") -join ";"
$runClasspath = @("bin") + $jars -join ";"
$compileLog = "bin\javac-positive-negative-stderr.log"

New-Item -ItemType Directory -Force bin | Out-Null
if (Test-Path $compileLog) {
    Remove-Item $compileLog -Force
}

javac -cp $compileClasspath -d bin src\*.java test\*.java 2> $compileLog

if ($LASTEXITCODE -ne 0 -and -not (Test-Path "bin\test\PositiveNegativeTestRunner.class")) {
    if (Test-Path $compileLog) {
        Get-Content $compileLog
    }
    exit $LASTEXITCODE
}

java -cp $runClasspath test.PositiveNegativeTestRunner
exit $LASTEXITCODE
