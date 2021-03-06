# Automatizaci贸n busqueda de productos en la pagina Web Dekosas
En este proyecto se desarrollo la automatizaci贸n en la b煤squeda de cinco (5) productos para la p谩gina [Dekosas.com](https://dekosas.com/co/), utilizando las herramientas de pruebas Selenium y Cucumber.

## Estructura del proyecto 馃搨
_En esta secci贸n encontrara los pasos b谩sicos para el desarrollo del proyecto_

* Explorar la pagina [Dekosas.com](https://dekosas.com/co/) y seleccionar los productos utilizados en la automatizaci贸n. Se crea un archivo en Excel  con el nombre de ``` retoDekosas.xlsx``` que contiene el nombre y precio de los productos.

![productosExcel](https://github.com/KaJuCave/imagenesDekosas/blob/master/productosExcel.PNG)

* Se crea un proyecto **Gradle** en el entorno de desarrollo.

* Para este proyecto se crear谩n paquetes y directorios para la automatizaci贸n. Comencemos creando cuatro paquetes en nuestro directorio **main/java** para los drivers del navegador, leer el archivo de Excel, los elementos que se utilizaran de la p谩gina y los pasos que se automatizaran en la p谩gina Dekosas.

![src](https://github.com/KaJuCave/imagenesDekosas/blob/master/src.PNG)

Dentro del fichero **test/java** se creara los paquetes para las clase de ejecuci贸n y definici贸n de los pasos dentro de la pagina Dekosas

![test](https://github.com/KaJuCave/imagenesDekosas/blob/master/test.PNG)

Por 煤ltimo en el directorio **resources** se creara la clase con la extensi贸n **.feature**, la cual contiene la descripci贸n de prueba que se va a ejecutar.

![resourse](https://github.com/KaJuCave/imagenesDekosas/blob/master/resourse.PNG)

_Nota: En las siguientes secciones se explicar谩n con m谩s detalle la codificaci贸n de cada una de sus clases_ 

* Adicionalmente se debe descargar el [chromedriver]( https://chromedriver.chromium.org/downloads) de acuerdo a la versi贸n del navegador, este le permitira que implementa el protocolo de WebDriver para Chromium. Por 煤ltimo, se agrega este driver y el archivo de Excel (con el nombre de los productos) a los archivos del proyecto.

![driverExcel](https://github.com/KaJuCave/imagenesDekosas/blob/master/driversExcel.PNG)

## Codificaci贸n del proyecto 馃搨馃捇

En esta sesi贸n se explicar谩 detalladamente la codificaci贸n que se implement贸 para automatizaci贸n de los productos de la p谩gina Dekosas.

### Agregar dependencias

Para que el correcto funcionamiento de algunas utilidades en el proyecto se debe agregar las dependencias en el archivo ```build.gradle``` que se muestran a continuaci贸n 

```gradle
apply plugin: 'java-library'
apply plugin: 'net.serenity-bdd.aggregator'
apply plugin: 'eclipse'

repositories {
    mavenLocal()
    mavenCentral()
    jcenter()

}

buildscript {
    repositories {
        mavenLocal()
        mavenCentral()
        jcenter()

    }
    dependencies {
        classpath("net.serenity-bdd:serenity-gradle-plugin:2.0.80")
    }
}

dependencies {
    implementation 'net.serenity-bdd:serenity-junit:2.0.80'
    implementation 'net.serenity-bdd:serenity-cucumber:1.9.45'
    implementation 'net.serenity-bdd:serenity-core:2.0.80'
    implementation 'org.slf4j:slf4j-simple:1.7.7'
    implementation group: 'org.apache.poi', name: 'poi', version: '3.17'
    implementation group: 'org.apache.poi', name: 'poi-ooxml', version: '3.17'
}

test {
    ignoreFailures = true
}
gradle.startParameter.continueOnFailure = true

```
### Archivo Excel

Para este proyecto se necesita leer los nombres de cada uno de los productos elegidos en la p谩gina por medio de un archivo Excel, por tal raz贸n se implement贸 la clase ```Excel.java``` y el siguiente m茅todo ```leerDatosDeHojaDeExcel ``` el cual recibe como par谩metro dos datos: ```String rutaDeExcel``` se especifica la ruta del archivo Excel con extension .xlsx (el cual se agreg贸 en pasos anteriores) y  el segundo ```String hojaDeExcel``` se refiere al nombre de la hoja del archivo Excel donde se guardaron los nombres de los productos.
Al finalizar los procesos de este m茅todo nos retorna una lista con los datos encontrados y solicitados del archivo Excel. 


```java
public class Excel {

    public static <rutaDeExcel, hojaExcel> ArrayList<Map<String, String>> leerDatosDeHojaDeExcel( String rutaDeExcel, String hojaDeExcel) throws IOException {
        ArrayList<Map<String, String>> arrayListDatoPlanTrabajo = new ArrayList<Map<String, String>>();
        Map<String, String> informacionProyecto = new HashMap<String, String>();
        File file = new File(rutaDeExcel);
        FileInputStream inputStream = new FileInputStream(file);
        XSSFWorkbook newWorkbook = new XSSFWorkbook(inputStream);
        XSSFSheet newSheet = newWorkbook.getSheet(hojaDeExcel);
        Iterator<Row> rowIterator = newSheet.iterator();
        Row titulos = rowIterator.next();
        while (rowIterator.hasNext()) {
            Row row = rowIterator.next();
            Iterator<Cell> cellIterator = row.cellIterator();
            while (cellIterator.hasNext()) {
                Cell cell = cellIterator.next();
                cell.getColumnIndex();
                switch (cell.getCellTypeEnum()) {
                    case STRING:
                        informacionProyecto.put(titulos.getCell(cell.getColumnIndex()).toString(), cell.getStringCellValue());
                        break;
                    case NUMERIC:
                        informacionProyecto.put(titulos.getCell(cell.getColumnIndex()).toString(), String.valueOf((long) cell.getNumericCellValue()));
                        break;
                    case BLANK:
                        informacionProyecto.put(titulos.getCell(cell.getColumnIndex()).toString(), "");
                        break;
                    default:
                }
            }
            arrayListDatoPlanTrabajo.add(informacionProyecto);
            informacionProyecto = new HashMap<String, String>();
        }
        return arrayListDatoPlanTrabajo;
    }
}
```
### Caracter铆sticas ChromeDriver

Para este proyecto se utilizar谩 el navegador **Google Chrome**, por esto creamos unos drivers que nos permitir谩n utilizar este navegador. Para realizar se cre贸 la clase ```GoogleChromeDriver.java``` y se instanci贸 un objeto de la interfaz **WebDriver** 

``` java
public static WebDriver driver;
```

Seguidamente se crea un m茅todo donde se especifican las opciones que tendr谩 el navegador como al iniciar la automatizaci贸n la ventana este maximizada y por ultimo se implementa el m茅todo ```driver.get(url) ```  el cual nos permite navegar a la URL pasada como argumento y espera hasta que se cargue la p谩gina.


```java
public class GoogleChromeDriver {

    public static WebDriver driver;

    public static void chomeWebDriver(String url) {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--start-maximized");
        options.addArguments("--ignore-certificate-errors");
        options.addArguments("--disable-infobars");
        driver = new ChromeDriver(options);
        driver.get(url);
    }
}

```
### Elementos y pasos en la p谩gina Dekosas

Como la p谩gina que utilizamos para la automatizaci贸n es [Dekosas.com](https://dekosas.com/co/) creamos una clase ```DekosasPage.java ``` en la cual creamos como atributos de la clase **By** los botones y los textos de los elementos a buscar en la p谩gina.

```java
By txtBuscador 
By btnBuscador 
By btnElementoBusqueda
By txtElementoBusqueda
```
De estos atributos inicializamos a ```txtBuscador ``` ``` btnBuscador``` con los respectivos **Xpath** que nos permitir谩n encontrar la barra de b煤squeda y escribir el producto en la p谩gina.

```java
By txtBuscador = By.xpath("//input[@id='search' and @name='q']");
By btnBuscador = By.xpath("//button[@class='amsearch-loupe' and @title='Buscar']");
```
Por 煤ltimo, se crean los ``` Getter ``` de todos los atributos, pero en los ``` Setter ``` solo los de ``` btnElementoBusqueda ``` y ``` txtElementoBusqueda``` se inicalizan como se muestra a continuaci贸n 
```java
public void setBtnElementoBusqueda(String producto) {
        this.btnElementoBusqueda = By.xpath("//a[contains(text(),'"+producto+"')]");
    }

    public void setTxtElementoBusqueda(String producto) {
        this.txtElementoBusqueda = By.xpath("//span[contains(text(),'"+producto+"')]");
    }

```
**DekosasSteps**

Dentro de la clase ``` DekosasSteps.java``` especificaremos los pasos que la p谩gina realizara en la automatizaci贸n. Para comenzar debemos instanciar objetos de las clases ``` DekosasPage```, ``` Excel``` y ``` ArrayList``` que contiene los datos del Excel. 

```java
DekosasPage dekosasPage = new DekosasPage();
Excel excelarchivo= new Excel();
ArrayList<Map<String, String>> datosExcel;

```
En los siguientes m茅todos se especifican los pasos que realizara el navegador en la automatizaci贸n

```java
public void abrirPagina(){
        GoogleChromeDriver.chomeWebDriver("https://dekosas.com/co/");
    }

    public void leerProductosDekosasExcel() {
        try {
            datosExcel = Excel.leerDatosDeHojaDeExcel("retoDekosas.xlsx","Productos");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void validarElementoEnPantalla(){
        for (int i = 0; i <= datosExcel.size()-1; i++) {
            GoogleChromeDriver.driver.findElement(dekosasPage.getTxtBuscador()).sendKeys(datosExcel.get(i).get("Nombre Producto"));
            GoogleChromeDriver.driver.findElement(dekosasPage.getTxtBuscador()).sendKeys(Keys.ENTER);
            dekosasPage.setBtnElementoBusqueda(datosExcel.get(i).get("Nombre Producto"));
            GoogleChromeDriver.driver.findElement(dekosasPage.getBtnElementoBusqueda()).click();
            dekosasPage.setTxtElementoBusqueda(datosExcel.get(i).get("Nombre Producto"));
            Assert.assertEquals(datosExcel.get(i).get("Nombre Producto"), GoogleChromeDriver.driver.findElement(dekosasPage.getTxtElementoBusqueda()).getText());
        }
    }

    public void cerrarNavegador(){
        GoogleChromeDriver.driver.quit();
    }
```
### Caracteriscticas de la automatizaci贸n (feature)

Se crea un archivo con nombre ``` DekosasBuscador``` y con extensi贸n ```.feature``` donde se describen las siguientes caracter铆sticas de la prueba. 

``` Feature: ```  nombre de la funcionalidad de la prueba. Con el caso de usuario que se va a probar 
``` Scenario: ``` se realiza para especificar la funcionalidad la b煤squeda de productos 
``` Given: ```  se describe el contexto, las precondiciones.
``` When: ```  se especifican las acciones que se van a ejecutar.
``` Then: ```  y ac谩 se especifica el resultado esperado, las validaciones a realizar.

```feature
Feature: HU-001 Buscador Dekosas
  Yo como usuario en la pagina web Dekosas
  Quiero buscar los productos en la plataforma
  Para ver las caracteristicas de los producto

  Scenario: Buscar productos
    Given me encuentro en la pagina web Dekosas
    When busque los productos
    Then puedo ver los productos en pantalla

```
### Definici贸n de los pasos (Steps Definitions)

Despu茅s de ejecutar por primera vez se crean los m茅todos ``` Given ``` ,``` When ```, ``` Then```  del proyecto. Estos m茅todos los implementamos en la clase ``` DekosaStepsDefinitions.java``` seguidamente se instancia un objeto de la clase ``` DekosasSteps ``` 

``` java
DekosasSteps dekosasSteps = new DekosasSteps();
```

Lo que faltar铆a en los m茅todos anteriores llamar los m茅todos que contiene los pasos para la automatizaci贸n como abrir el navegador, leer los productos que est谩n guardados en el archivo de Excel, validar los productos encontrados en pantalla y por 煤ltimo cerrar el navegador cuando la automatizaci贸n finalice de forma exitosa.

```java

    @Given("^me encuentro en la pagina web Dekosas$")
    public void meEncuentroEnLaPaginaWebDekosas() {
        dekosasSteps.abrirPagina();

    }
    @When("^busque los productos$")
    public void busqueLosProductos() {
        dekosasSteps.leerProductosDekosasExcel();
    }

    @Then("^puedo ver los productos en pantalla$")
    public void puedoVerLosProductosEnPantalla() {
        dekosasSteps.validarElementoEnPantalla();
        dekosasSteps.cerrarNavegador();
    }
```

## Ejecuci贸n 馃捇

Despu茅s de realizar la codificaci贸n que se explic贸 anteriormente se  _ejecutar_ el proyecto en desde la clase **DekosasBuscadorRunner.java**, donde se defini贸 los siguientes par谩metros:

* @RunWith :  Es el runner de cucumber con serenity ejecutar谩 todas las funciones que se encuentran en la ruta de clase en el mismo paquete que esta clase.

*  @CucumberOptions :  Es propio de cucumber. Tiene varias opciones de configuraci贸n, para este proyecto utilizamos:

	* features: Se coloca la direcci贸n con la ubicar del archivo de caracter铆sticas en la carpeta del proyecto.
	* glue: Es muy parecido a la opci贸n anterior, pero la diferencia es que ayuda a Cucumber a localizar el archivo con la **definici贸n de pasos**. Para este proyecto es la clase **stepsDefinitions**.
	* snippets: Es el formato de los fragmentos del c贸digo que genera Cucumber, para este caso se elige le tipo CAMELCASE.

``` java
@RunWith(CucumberWithSerenity.class)
@CucumberOptions(
        features = "src\\test\\resources\\features\\DekosasBuscador.feature",
        glue = "stepsDefinitions",
        snippets = SnippetType.CAMELCASE
)
public class DekosasBuscadorRunner {
}

```
## Construido con 馃洜锔?

_En este proyecto se utilizaron las siguientes herramientas_

* [IntelliJ IDEA ](https://www.jetbrains.com/es-es/idea/) - El entorno de desarrollo usado
* [Gradle](https://gradle.org/) - Sistema de automatizaci贸n  
* [Cucumber](https://cucumber.io/) - Software que aplica el desarrollo impulsado por el comportamiento
* [Selenium](https://www.selenium.dev/) - Entorno de pruebas de software para aplicaciones basadas en la web

# Demo 馃摻锔?
Para ver la demostracion de la ejecuci贸n del proyecto puede consultar en el video [automatizacion Dekosas](https://youtu.be/zirpW6UrqR8)

---
Elaborado por: [Juliana Cano Vega](https://github.com/KaJuCave)馃捇馃懇馃徎
