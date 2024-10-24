package com.example.ejemplodatabase

import android.os.Bundle
import android.widget.Space
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.lifecycleScope
import com.example.ejemplodatabase.data.NombreDataBase
import com.example.ejemplodatabase.data.NombreEntity
import com.example.ejemplodatabase.data.NombreViewModel
import com.example.ejemplodatabase.ui.theme.EjemploDataBaseTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val viewModel : NombreViewModel by viewModels()
        setContent {
            EjemploDataBaseTheme {
                Column {
                    Spacer(Modifier.height(30.dp))
                    //AgregarNombre(viewModel)
                    Boton(viewModel)
                    Spacer(Modifier.height(30.dp))
                    Listar(viewModel)
                }
            }
        }
    }
}

@Composable
fun Listar(viewModel: NombreViewModel){
    val nombres by viewModel.getNombres().asFlow().collectAsState(
        initial = emptyList()
    )
    List(nombres)
}

@Composable
fun List(nombre: List<NombreEntity>){
    nombre.forEach{
        Text(it.name)
    }
}

@Composable
fun AgregarNombre(viewModel : NombreViewModel){
    var texto by remember { mutableStateOf("") }
    Column {
        TextField(
            value = texto,
            onValueChange = {
                texto = it
            },
            label ={ Text("Agregar nombres")}
        )
    }

    Button(
         onClick ={
            viewModel.insertName(NombreEntity(name = texto))
            texto = ""
        }
    ){Text("Agregar nombre")}
}


@Composable
fun Boton(viewModel : NombreViewModel) {

    var name by remember { mutableStateOf<String>("") }
    var selectedName by remember { mutableStateOf("") }
    var names by remember { mutableStateOf(listOf<String>()) }

    Column {

        TextField(
            value = name,
            onValueChange = { nombre ->
                name = nombre
            },
            label = {
                Text("Ingrese un nombre")
            }
        )
        Spacer(modifier = Modifier.height(14.dp))
        Lista(names = names)
        Button(
            onClick = {
                if(name.isNotBlank()){
                    names = names + name
                    name = ""
                }
            }
        ){
            Text("Agregar un nuevo nombre")
        }

        if(names.isNotEmpty()){
            Button (
                onClick = {
                    selectedName = names.random()
                    val nombre = NombreEntity(
                        name=selectedName
                    )
                    viewModel.insertName(nombre)
                }
            ){
                Text("Seleccionar nombre al azar")
            }
        }

        if (selectedName.isNotBlank()){
            Text("El nombre seleccionado es : $selectedName")
        }
    }
}

@Composable
fun Lista(
    modifier : Modifier = Modifier,
    names: List<String>
){
    Column{
        names.forEach{ name ->
            Box(
                modifier = Modifier.background(color = Color.LightGray)
            ){
                Text(name)
            }
        }
    }
}
