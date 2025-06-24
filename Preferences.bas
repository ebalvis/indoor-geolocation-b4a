B4A=true
Group=Default Group
ModulesStructureVersion=1
Type=StaticCode
Version=12.8
@EndOfDesignText@
'Code module
'Subs in this code module will be accessible from all modules.
Sub Process_Globals
	'These global variables will be declared once when the application starts.
	'These variables can be accessed from all modules.
	Dim manager As PreferenceManager
	Dim screen As PreferenceScreen
	Dim cat1, cat2,cat3,cat4,cat5 As PreferenceCategory
End Sub


Sub ShowPreferences
	StartActivity(screen.CreateIntent)
End Sub

Sub CreatePreferenceScreen
	screen.Initialize("Configuración", "")
	'create two categories
	
	cat1.Initialize("Servidor MQTT")
	cat1.AddEditText( "edtServer", "Servidor", "IP del servidor", "")
	cat1.AddEditText( "edtPort", "Puerto", "Puerto servidor", "")
	cat1.AddEditText("edtUsuario", "Usuario", "Nombre usuario ", "")
	cat1.AddEditText("edtPassword", "Password", "Clave usuario", "")
	Dim valuesLevel As List
	valuesLevel.Initialize2(Array As String("Sotano 1","Bajo","Planta 1","Planta 2","Planta3","Planta 4"))
	cat2.Initialize("Plantas edificio ESEI")
	cat2.AddList("levelBuild","Nivel","Nivel edificio","Bajo",valuesLevel )

	'add the categories to the main screen
	screen.AddPreferenceCategory(cat1)
	screen.AddPreferenceCategory(cat2)
	If manager.GetAll.Size = 0 Then SetDefaults
End Sub

Sub SetDefaults 'http://193.147.87.88:18123/
	manager.SetString("edtServer","193.147.87.88")
	manager.SetString("edtPort","8080")
	manager.SetString("edtUsuario","admin")
	manager.SetString("edtPassword","admin")
	manager.SetString("levelBuild","Bajo")
End Sub
