#To be run with Powershell.exe -executionpolicy remotesigned -File keep-alive.ps1
mkdir keep-alive
For ($i=1; $i -lt 41; $i++)  {
(new-object System.Net.WebClient).DownloadFile("http://lolin1-data-provider-eu$i.herokuapp.com/services/champions/list?realm=na&locale=en_US","keep-alive\listeu$i")
}
For ($j=1; $j -lt 11; $j++)  {
(new-object System.Net.WebClient).DownloadFile("http://lolin1-data-provider-usa$j.herokuapp.com/services/champions/list?realm=na&locale=en_US","keep-alive\listusa$j")
}
rmdir keep-alive -s -q