[General]
SyntaxVersion=2
BeginHotkey=121
BeginHotkeyMod=0
PauseHotkey=0
PauseHotkeyMod=0
StopHotkey=123
StopHotkeyMod=0
RunOnce=0
EnableWindow=
MacroID=1de65abe-b29b-4741-b570-9a9d064c3be7
Description=抢如意红包
Enable=1
AutoRun=0
[Repeat]
Type=1
Number=1
[SetupUI]
Type=2
QUI=
[Relative]
SetupOCXFile=
[Comment]

[Script]
//红包的四个点，精准判断是否为红包
hbzs_x = 0
hbzs_y = 0
hbyx_x = 490
hbyx_y = 798

//输入框坐标，用于获取到红包后发一段文字
input_x = 274
input_y = 732
hb_t = 0
TracePrint "脚本开始执行"
Rem A
//获取是否有红包图片
FindPic 0,0,500,798,"E:\gitSource\bjpk2\sc\拆红包.bmp",0.9,intX,intY

//获取是否有确定按钮
FindPic 0,0,500,798,"E:\gitSource\bjpk2\sc\确定.bmp",0.9,qdX,qdY
If qdX > 0 And qdY > 0 Then 
	//出现了确定按钮要点击确定
	MoveTo intX,intY
	LeftClick 1
End If

//MessageBox cr1
If intX > 0 And intY > 0 Then 
	//出现了红包则点击红包
	MoveTo intX,intY
	LeftClick 1
	MoveTo input_x,input_y
	LeftClick 1
	SayString "红包不给力啊..."
	KeyPress "Enter", 1
	hb_t = 0
	TracePrint "已抢中一次"
End If
Delay 3000
hb_t = hb_t + 3000

If hb_t > 5400000 Then 
	MoveTo input_x,input_y
	LeftClick 1
	SayString "管理发红包啦~"
	KeyPress "Enter", 1
	hb_t = 0
	TracePrint "已催管理员发红包"
End If
goto A
      