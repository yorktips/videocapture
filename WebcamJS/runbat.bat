set arg1=%1
set arg2=%2

rem https://social.technet.microsoft.com/Forums/scriptcenter/en-US/783cd29c-8b92-46d1-9bb8-4f602adfe8e7/iexpress-passing-parameters-to-applaunched?forum=ITCG

echo %arg1% > ./para.log.txt
rem echo %arg2% >> ./para.log.txt

"C:\Program Files (x86)\Mozilla Firefox\firefox.exe" "./capture.html"  