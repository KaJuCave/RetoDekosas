package stepsDefinitions;

import cucumber.api.PendingException;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import steps.DekosasSteps;

public class DekosaStepsDefinitions {

    DekosasSteps dekosasSteps = new DekosasSteps();

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


}
