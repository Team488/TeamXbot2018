$thingsToTest = Get-ChildItem .\src\main -Recurse | select Name | Where-Object {$_ -like "*Command*.java*" -or $_ -like "*Subsystem*.java*"}
$expectedTestsToFind = $thingsToTest | ForEach-Object {$_.Name -replace ".java", "Test.java"}


$actualTests = Get-ChildItem .\src\test -Recurse | select Name | Where-Object {$_ -like "*Command*.java*" -or $_ -like "*Subsystem*.java*"}


foreach ($wantedTest in $expectedTestsToFind) {
    $foundTest = $false
    foreach ($actualTest in $actualTests) {
       if ($actualTest.Name -eq $wantedTest) {
        $foundTest = $true
       }
    }

    if ($foundTest -eq $false) {
        Write-Host $wantedTest " does not exist!"
    }
}