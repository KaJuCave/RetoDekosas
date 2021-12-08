Feature: HU-001 Buscador Dekosas
  Yo como usuario en la pagina web Dekosas
  Quiero buscar un producto en la plataforma
  Para ver las caracteristicas del producto

  Scenario Outline: Buscar producto
    Given me encuentro en la pagina web Dekosas
    When busque el producto <NombreProducto>
    Then puedo ver <NombreProducto> en pantalla
    Examples:
      |NombreProducto|
      |Pinchos Punga – Mulikka|