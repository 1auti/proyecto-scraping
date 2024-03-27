package com.chatbotconscraping.Service.Impl;

import com.chatbotconscraping.Entity.*;
import com.chatbotconscraping.Repository.ProductoRepository;
import com.chatbotconscraping.Service.ProductoService;
import jakarta.transaction.Transactional;
import org.apache.commons.io.FileUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class ProductoServiceImpl implements ProductoService {

    @Autowired
    ProductoRepository productoRepository;
    @Override
    public Producto obtenerProductoJumbo(String producto) {

        String nombreProducto = producto.toLowerCase().replace(" ","-");
        Producto productoAux = new Producto();
        try {
            //ASIGNAMOS LA URL
            String url = "https://www.jumbo.com.ar/" + nombreProducto + "/p";

            //CONECTAMOS A LA URL
            Document document = Jsoup.connect(url).get();

            //OBTENER PRECIO
            String html1 = "<span style=\"display: flex; align-items: center;\">" +
                    "<div class=\"jumboargentinaio-store-theme-1oaMy8g_TkKDcWOQsx5V2i\">" +
                    "<div class=\"jumboargentinaio-store-theme-1dCOMij_MzTzZOCohX1K7w\">$7.057,7</div>" +
                    "</div>" +
                    "<div title=\"PR_PRIME 35%_CAFE | Descuento por Estructura\">" +
                    "<div>" +
                    "<span class=\"jumboargentinaio-store-theme-2kMle1PVrzvFULot4LFRno\">-35%</span>" +
                    "<img class=\"jumboargentinaio-store-theme-1Rbh8fPR7Te3j33X9yy0pK\" src=\"https://jumboargentinaio.vtexassets.com/arquivos/prime-logo.png\" crossorigin=\"anonymous\">" +
                    "</div>" +
                    "</div>" +
                    "</span>";

            // Parsea el HTML
            Document document1 = Jsoup.parse(html1); //precio del producto

            String html2 = "<div class=\"view-conditions_certificados-container\"><div style=\"position: relative; display: inline-block;\"><div class=\"view-conditions_card_certificate\"><img src=\"https://jumboargentinaio.vtexassets.com/arquivos/vegan.svg\" crossorigin=\"anonymous\"></div></div><div style=\"position: relative; display: inline-block;\"><div class=\"view-conditions_card_certificate\"><img src=\"https://jumboargentinaio.vtexassets.com/arquivos/vegetarian.svg\" crossorigin=\"anonymous\"></div></div><div style=\"position: relative; display: inline-block;\"><div class=\"view-conditions_card_certificate\"><img src=\"https://jumboargentinaio.vtexassets.com/arquivos/gluten_free.svg\" crossorigin=\"anonymous\"></div></div><" + "/div>";
            Document document2 = Jsoup.parse(html2);



            //OBTENEMOS LOS DATOS
            Element elementoNombre = document.selectFirst("span.vtex-store-components-3-x-productBrand");
            Element elementoMarca = document.selectFirst("span.vtex-store-components-3-x-productBrandName");
            Element elementoPrecio = document1.selectFirst("div.jumboargentinaio-store-theme-1dCOMij_MzTzZOCohX1K7w");
            Element elementoImagen = document.selectFirst("img.vtex-store-components-3-x-productImageTag");


            //OBTENEMOS LAS CARACTERISTICAS DEL PRODUCTO
            List<CaracteristicasProducto> caracteristicasProductos = extraerCaracteristicasProducto(document,productoAux);
            //OBTENEMOS LOS VALORES NUTRICIONALES
            List<ValorNutricional> valorNutricionalList = extraerValorNutricional(document,productoAux);
            //OBTENEMOS LA DESCRIPCION DE LOS PRODUCTOS
            List<Descripcion> descripcionList = extraerDescripcion(document,productoAux);
            //OBTENEMOS LOS CERTEFICADOS
            List<Certificado> certificadoList = obtenerCertificados(document2,productoAux,elementoNombre.text());
            //ASIGNAMOS LAS CARACTERISTICAS Y VALOR NUTRICIONAL
            productoAux.setCaracteristicas(caracteristicasProductos);
            productoAux.setValorNutricional(valorNutricionalList);
            productoAux.setDescripcion(descripcionList);
            productoAux.setCertificados(certificadoList);



            //VALIDAMOS LOS DATOS Y LOS ASIGNAMOS
            if (elementoNombre != null && elementoPrecio != null && elementoMarca != null && elementoImagen != null ) {
                productoAux.setNombre(elementoNombre.text());
                productoAux.setPrecio(elementoPrecio.text());
                productoAux.setMarca(elementoMarca.text());
                productoAux.setSupermercado("Jumbo");

                //Proceso para guardar la imagen
                String img = elementoImagen.attr("src");

                descargarImagen(img,elementoNombre.text(),productoAux);

                productoRepository.save(productoAux);

                return (productoAux);
            }

        } catch (IOException e) {
            e.printStackTrace();


        }

        return productoAux;
    }

    @Override
    public List<Producto> traerProductos() {
        return productoRepository.findAll();
    }

    @Override
    public Producto findProducto(String nombre) {
        return productoRepository.findProducto(nombre);
    }


    //DESCARGAMOS LA IMAGEN
    private void descargarImagen(String imgUrl, String nombreArchivo,Producto producto) throws IOException{

       String destinationFile = "C://Users//Lautaro//Desktop//Desarrollo-web//Proyectos//proyecto-chatbot-con-scraping//imagenes-productos-jumbo//" + nombreArchivo;
        try {

            // Verificar si el directorio de destino existe, si no, crearlo
            File directorio = new File(destinationFile);
            if (!directorio.exists()) {
                directorio.mkdirs();
            }

            // Agregar la extensión del archivo a nombreArchivo si no está presente
            if (!nombreArchivo.toLowerCase().endsWith(".jpg")) {
                nombreArchivo += ".jpg";
            }

            // Construir la ruta completa del archivo de destino
            String rutaArchivo = destinationFile + File.separator + nombreArchivo;

            // Descargar la imagen desde la URL
            URL url = new URL(imgUrl);
            URLConnection conn = url.openConnection();
            try (InputStream inputStream = conn.getInputStream();
                 OutputStream outputStream = new FileOutputStream(rutaArchivo)) {

                byte[] buffer = new byte[4096];
                int bytesRead;
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }

                producto.setImg(nombreArchivo);

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //EXTRAEMOS CARACTERISTICAS PRODUCTOS
    private static List<CaracteristicasProducto> extraerCaracteristicasProducto(Document document,Producto producto) {

        List<CaracteristicasProducto> caracteristicasProductoList = new ArrayList<>();

        // Seleccione el elemento <div> con la clase 'view-new_div' que contiene las características del producto
        Element div = document.selectFirst("div.view-new_div");

        if (div != null) {
            // Seleccione todos los elementos <li> dentro del <ul> dentro del <div>
            Elements elementosLi = div.select("ul > li");

            // Iterar sobre los elementos <li> para extraer las características
            for (int i = 0; i < elementosLi.size(); i += 2) {
                // Obtener el primer <li> como nombre y el segundo <li> como valor
                Element liNombre = elementosLi.get(i);
                Element liValor = i + 1 < elementosLi.size() ? elementosLi.get(i + 1) : null;

                // Crear un objeto CaracteristicasProducto y asignar el nombre y el valor
                CaracteristicasProducto caracteristica = new CaracteristicasProducto();
                caracteristica.setNombre(liNombre.text());
                caracteristica.setProducto(producto);

                // Si hay un segundo <li>, asignar su texto como valor
                if (liValor != null) {
                    caracteristica.setValor(liValor.text());

                }

                // Agregar el objeto CaracteristicasProducto a la lista
                caracteristicasProductoList.add(caracteristica);

            }
        }

        return caracteristicasProductoList;
    }

    //EXTREAMOS VALOR NUTRIOCIONAL
    private static List<ValorNutricional> extraerValorNutricional(Document document, Producto producto) {
        List<ValorNutricional> valorNutricionalList = new ArrayList<>();

        try {
            Element nutricionContainer = document.selectFirst("div.view-conditions_nutricional_table_rows");

            if (nutricionContainer != null) {
                // Obtener todos los elementos <ul> dentro del contenedor
                Elements ulElements = nutricionContainer.select("ul");

                // Iterar sobre cada elemento <ul>
                for (Element ul : ulElements) {

                    // Obtener el texto del primer <li> dentro del <ul> como nombre del nutriente
                    String nombreNutriente = ul.selectFirst("li.v-c_table_row_name").text().trim();

                    if(!nombreNutriente.isEmpty()){

                        // Obtener los valores del valor nutricional por cada 100g/ml y por porción
                        Elements valueElements = ul.select("li.v-c_table_row_value");
                        String valorPor100g = valueElements.get(0).text().trim();
                        String valorPorPorcion = valueElements.get(1).text().trim();

                        // Crear un objeto Nutriente y agregarlo a la lista
                        ValorNutricional valorNutricional = new ValorNutricional();
                        valorNutricional.setValorMedio(nombreNutriente);
                        valorNutricional.setPorCada100gMl(valorPor100g);
                        valorNutricional.setPorCada1Porcion(valorPorPorcion);
                        valorNutricional.setProducto(producto);

                        valorNutricionalList.add(valorNutricional);
                    }

                }


            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return valorNutricionalList;
    }

    //EXTRAEMOS LA DESCRIPCION
    private static List<Descripcion> extraerDescripcion(Document document, Producto producto) {
        List<Descripcion> ingredientesList = new ArrayList<>();

        try {
            // Buscar el elemento <div> que contiene los ingredientes
            Element divElement = document.selectFirst("div.v-c_descripcion_subtitle:has(span:contains(Ingredientes)) + article");

            if (divElement != null) {
                // Obtener el texto dentro del <article> que contiene los ingredientes
                Element articleElement = divElement.selectFirst("article");

                if (articleElement != null) {
                    String articulo = articleElement.text().trim();

                    Descripcion descripcion = new Descripcion();
                    descripcion.setIngrediente(articulo);
                    descripcion.setProducto(producto);

                    // Agregar el texto de los ingredientes a la lista
                    ingredientesList.add(descripcion);
                } else {
                    System.out.println("No se encontró el artículo de los ingredientes.");
                }
            } else {
                System.out.println("No se encontró el elemento div que contiene los ingredientes.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return ingredientesList;
    }

    private static List<Certificado> obtenerCertificados(Document document, Producto producto, String nombreArchivo) {
        List<Certificado> certificadoList = new ArrayList<>();

        // Crear el directorio de destino si no existe
        String destinationFolder = "C://Users//Lautaro//Desktop//Desarrollo-web//Proyectos//proyecto-chatbot-con-scraping//imagenes-productos-jumbo//" + nombreArchivo + "//Certificados";
        File directorio = new File(destinationFolder);
        if (!directorio.exists()) {
            directorio.mkdirs();
        }

        // Buscar el div que contiene las imágenes de los certificados
        Element certificadosContainer = document.selectFirst(".view-conditions_certificados-container");

        if (certificadosContainer != null) {
            // Obtener todas las imágenes de los certificados
            Elements imagenesCertificados = certificadosContainer.select("img");

            // Iterar sobre las imágenes y obtener sus atributos src
            for (Element img : imagenesCertificados) {
                try {
                    String src = img.attr("src");

                    // Obtener el nombre de archivo de la URL de la imagen
                    String nombreImagen = obtenerNombreArchivo(src);

                    // Descargar la imagen y guardarla en el directorio
                    URL url = new URL(src);
                    String rutaArchivo = destinationFolder + File.separator + nombreImagen;
                    File imagenFile = new File(rutaArchivo);
                    FileUtils.copyURLToFile(url, imagenFile);

                    // Crear un nuevo certificado con la información obtenida
                    Certificado certificado = new Certificado();
                    certificado.setImg(nombreImagen); // Guardar el nombre del archivo
                    certificado.setProducto(producto);

                    // Agregar el certificado a la lista
                    certificadoList.add(certificado);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return certificadoList;
    }

    // Función para obtener el nombre de archivo de una URL
    private static String obtenerNombreArchivo(String url) {
        String[] partesUrl = url.split("/");
        return partesUrl[partesUrl.length - 1];
    }



}











