package pages;

import org.openqa.selenium.By;

public class DekosasPage {

    By txtBuscador = By.xpath("//input[@id='search' and @name='q']");
    By btnBuscador = By.xpath("//button[@class='amsearch-loupe' and @title='Buscar']");
    By btnElementoBusqueda;
    By txtElementoBusqueda;

    public By getTxtBuscador() {
        return txtBuscador;
    }

    public By getBtnBuscador() {
        return btnBuscador;
    }

    public By getBtnElementoBusqueda() {
        return btnElementoBusqueda;
    }

    public By getTxtElementoBusqueda() {
        return txtElementoBusqueda;
    }

    public void setBtnElementoBusqueda(String producto) {
        this.btnElementoBusqueda = By.xpath("//a[contains(text(),'"+producto+"')]");
    }

    public void setTxtElementoBusqueda(String producto) {
        this.txtElementoBusqueda = By.xpath("//span[contains(text(),'"+producto+"')]");
    }
}

