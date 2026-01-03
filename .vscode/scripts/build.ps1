# Ensure bin exists
New-Item -ItemType Directory -Path bin -Force

# Collect java files and compile
$files = Get-ChildItem -Path main,test -Recurse -Filter *.java | ForEach-Object FullName
if ($files) {
    javac -d bin -cp 'lib/junit-platform-console-standalone-1.14.1.jar' $files
} else {
    Write-Output 'No Java source files found.'
}