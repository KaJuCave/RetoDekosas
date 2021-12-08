package steps;

import drivers.GoogleChromeDriver;
import modeloExcel.Excel;
import org.junit.Assert;
import org.openqa.selenium.Keys;
import pages.DekosasPage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

public class DekosasSteps {

    DekosasPage dekosasPage = new DekosasPage();
    Excel excelarchivo= new Excel();
    ArrayList<Map<String, String>> datosExcel;

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
}

