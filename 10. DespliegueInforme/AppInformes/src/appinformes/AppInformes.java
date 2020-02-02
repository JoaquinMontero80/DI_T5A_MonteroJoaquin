/***********************************************************************************************************************
 * Joaquin Montero. 2DAM.   DI_T5A10                                                                                ***
 * Cada alumno creará un proyecto javaFX que incluya un menú principal, MenuBar. En dicho                           ***
 * MenuBar debe haber al menos una opción que se llamará Informes. Dentro de esta opción se                         ***
 * desplegarán en cascada (vertical) las siguientes opciones (MenuItem) que deberán generar los                     ***
 * informes que se detallan a partir de la base de datos de prueba (test) de Jaspersoft Studio:                     ***
 *      1. Listado Facturas. Se mostrará un informe llamado "facturas", en el que se detalle los datos              ***
 *         personales (nombre, apellidos y dirección) de los clientes de la empresa (tabla ADDRESS) y un            ***
 *         listado con las facturas que han generado (cada factura es un documento). Para la factura aparece        ***
 *         el listado de productos (su nombre), la cantidad y el precio, y el importe total de cada producto.       ***
 *         Añadir a cada factura el importe total a pagar. Añadir el importe total para cada cliente también.       ***
 *      2. Ventas Totales. Se mostrará un informe llamado "Ventas Totales", en el que se un listado con             ***
 *         las ventas totales (cantidad total) de cada producto (se visualiza su nombre). Añadir un gráfico al      ***
 *         informe que compare estos totales. Se debe seleccionar el tipo de gráfico que mejor se adapte al         ***
 *         problema.                                                                                                ***
 *      3. Facturas por Cliente. Informe para generar facturas, teniendo en cuenta que el código del                ***
 *         cliente (addressid) se pasa al informe como parámetro.                                                   ***
 *      4. Subinforme Listado Facturas. Repite el punto 1 (sin subtotales) utilizando subinformes.                  ***
 *                                                                                                                  ***
 **********************************************************************************************************************/

package appinformes;

import java.awt.TextField;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javax.swing.JOptionPane;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.view.JasperViewer;

/**
 *
 * @author joaquin
 */
public class AppInformes extends Application 
{
    // Variables globales
    ConectaDB conexion = new ConectaDB(); 
    Connection cx = conexion.getConexion(); 
    
    
    @Override
    public void start(Stage primaryStage) 
    {
        VBox root = new VBox();
        MenuItem listadoFacturas = new MenuItem("1. Listado Facturas");
        MenuItem ventasTotales = new MenuItem("2. Ventas Totales");
        MenuItem facturaPorCliente = new MenuItem("3. Factura por Cliente");
        MenuItem subInformeListado = new MenuItem("4. Subinforme Listado Facturas");
        MenuItem salir = new MenuItem("Salir");
        Menu informes = new Menu("Informes");
        informes.getItems().addAll(listadoFacturas, ventasTotales, facturaPorCliente, subInformeListado);
        Menu salida = new Menu("Exit");
        salida.getItems().addAll(salir);
        MenuBar mb = new MenuBar(informes, salida);
        
        // TextField para cuando se le pide parámetros
        TextField tintro = new TextField("Introduce ID de cliente.");
        
        // Cuando pulsa en MenuItem listadoFacturas genera un evento que llama a la vez a un metodo (idem con demás)
        listadoFacturas.setOnAction(new EventHandler<ActionEvent>()
        {
            @Override
            public void handle(ActionEvent event)
            {
                try
                {
                    listadoFacturas(cx);
                }
                catch(JRException ex)
                {
                    Logger.getLogger(AppInformes.class.getName()).log(Level.SEVERE, null, ex);
                }
                
            }
        });
        
        ventasTotales.setOnAction(new EventHandler<ActionEvent>()
        {
            @Override
            public void handle(ActionEvent event)
            {
                try
                {
                    ventasTotales(cx);
                }
                catch(JRException ex)
                {
                    Logger.getLogger(AppInformes.class.getName()).log(Level.SEVERE, null, ex);
                }
                
            }
        });
        
        facturaPorCliente.setOnAction(new EventHandler<ActionEvent>()
        {
            @Override
            public void handle(ActionEvent event)
            {
                try
                {
                    generaInforme(tintro);
                }
                catch(JRException ex)
                {
                    Logger.getLogger(AppInformes.class.getName()).log(Level.SEVERE, null, ex);
                }
                
            }
        });
        
        subInformeListado.setOnAction(new EventHandler<ActionEvent>()
        {
            @Override
            public void handle(ActionEvent event)
            {
                try
                {
                    subInformeListado(cx);
                }
                catch(JRException ex)
                {
                    Logger.getLogger(AppInformes.class.getName()).log(Level.SEVERE, null, ex);
                }
                
            }
        });
        
        // Funcion de salir
        salir.setOnAction(e -> {
            System.exit(0); 
        }); 
        
        // Crea escena 
        Scene scene = new Scene(root, 350, 250);
        scene.setFill(Color.MAGENTA);  
        ((VBox) scene.getRoot()).getChildren().addAll(mb);
        
        // Imagen de fondo
        Image img = new Image(getClass().getResourceAsStream("fondo.jpg"));
        ImageView im = new ImageView(img);
        ((VBox) scene.getRoot()).getChildren().addAll(im);
        
        primaryStage.setTitle("AppInformes");
        primaryStage.setScene(scene);
        primaryStage.show();  
        
    } // Fin de metodo start
    
    
    /**
    * @param args the command line arguments
    */
    public static void main(String[] args) {
    launch(args);
    }

//     METODOS POR CADA SELECCION EN MENU
    
    public void listadoFacturas(Connection cx) throws JRException
    { 
        try 
        {
            JasperReport jr = (JasperReport)JRLoader.loadObject(AppInformes.class.getResource("Facturas.jasper"));
            //Map de parámetros
            Map parametros = new HashMap();
            // int nproducto = Integer.valueOf(tintro.getText());
            // parametros.put("ParamProducto", nproducto);
            // No le pasa parametros (null)
            JasperPrint jp = (JasperPrint) JasperFillManager.fillReport(jr, null, cx);
            // JasperViewer.viewReport(jp);
            JasperViewer jv = new JasperViewer(jp); 
            jv.setDefaultCloseOperation(JasperViewer.DISPOSE_ON_CLOSE);
            jv.setTitle("Facturas");
            jv.setExtendedState(JasperViewer.MAXIMIZED_BOTH);
            jv.requestFocus();
            jv.setVisible(true);
        } 
        catch (JRException ex) 
        {
            System.out.println("Error al recuperar el jasper");
            JOptionPane.showMessageDialog(null, ex);
        }
    } // Fin de metodo
    
    
    public void ventasTotales(Connection cx) throws JRException
    { 
        try 
        {
            JasperReport jr2 = (JasperReport)JRLoader.loadObject(AppInformes.class.getResource("Ventas_Totales.jasper"));
            //Map de parámetros
            Map parametros = new HashMap();
            // int nproducto = Integer.valueOf(tintro.getText());
            // parametros.put("ParamProducto", nproducto);
            // No le pasa parametros (null)
            JasperPrint jp2 = (JasperPrint) JasperFillManager.fillReport(jr2, null, cx);
            // JasperViewer.viewReport(jp);
            JasperViewer jv2 = new JasperViewer(jp2); 
            jv2.setDefaultCloseOperation(JasperViewer.DISPOSE_ON_CLOSE);
            jv2.setTitle("Ventas Totales");
            jv2.setExtendedState(JasperViewer.MAXIMIZED_BOTH);
            jv2.requestFocus();
            jv2.setVisible(true);
        } 
        catch (JRException ex) 
        {
            System.out.println("Error al recuperar el jasper");
            JOptionPane.showMessageDialog(null, ex);
        }
    } // Fin de metodo
    
    
    public void generaInforme(TextField tintro) throws JRException
    { 
        try 
        {
            JasperReport jr3 = (JasperReport)JRLoader.loadObject(AppInformes.class.getResource("Facturas_por_Clientes.jasper"));
            //Map de parámetros
            Map parametros = new HashMap();
            int nproducto = Integer.valueOf(tintro.getText());
            parametros.put("ParamProducto", nproducto);
            JasperPrint jp3 = (JasperPrint) JasperFillManager.fillReport(jr3, parametros, cx);
            JasperViewer.viewReport(jp3);
            JasperViewer jv3 = new JasperViewer(jp3); 
            jv3.setDefaultCloseOperation(JasperViewer.DISPOSE_ON_CLOSE);
            jv3.setTitle("Facturas por Cliente");
            jv3.setExtendedState(JasperViewer.MAXIMIZED_BOTH);
            jv3.requestFocus();
            jv3.setVisible(true);
        } 
        catch (JRException ex) 
        {
            System.out.println("Error al recuperar el jasper");
            JOptionPane.showMessageDialog(null, ex);
        }
    } // Fin de metodo
    
    
    public void subInformeListado(Connection cx) throws JRException
    { 
        try 
        {
            JasperReport jr4 = (JasperReport)JRLoader.loadObject(AppInformes.class.getResource("SubinformeListadoFacturas.jasper"));
            //Map de parámetros
            Map parametros = new HashMap();
            // int nproducto = Integer.valueOf(tintro.getText());
            // parametros.put("ParamProducto", nproducto);
            // No le pasa parametros (null)
            JasperPrint jp4 = (JasperPrint) JasperFillManager.fillReport(jr4, null, cx);
            // JasperViewer.viewReport(jp);
            JasperViewer jv4 = new JasperViewer(jp4); 
            jv4.setDefaultCloseOperation(JasperViewer.DISPOSE_ON_CLOSE);
            jv4.setTitle("Subinforme Listado Facturas.");
            jv4.setExtendedState(JasperViewer.MAXIMIZED_BOTH);
            jv4.requestFocus();
            jv4.setVisible(true);
        } 
        catch (JRException ex) 
        {
            System.out.println("Error al recuperar el jasper");
            JOptionPane.showMessageDialog(null, ex);
        }
    } // Fin de metodo
    
} // Fin de clase

