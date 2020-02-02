/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package appinformes;

import java.sql.*;


/**
 *
 * @author joaquin
 */
public class ConectaDB 
{
    // Atributos
    private Connection conexion;
    private String baseDatos = "jdbc:hsqldb:hsql://localhost:9001/xdb";
    private String usuario = "SA";
    private String clave = ""; 
    
    // Constructor por defecto
    public ConectaDB()
    {
        try
        {
            Class.forName("org.hsqldb.jdbcDriver").newInstance();
            // Establece la conexion
            conexion = DriverManager.getConnection(baseDatos,usuario,clave);
            System.out.println("Conexión establecida.");
        }
        catch (ClassNotFoundException cnfe)
        {
            System.err.println("Fallo al cargar JDBC");
            System.exit(1);
        }
        catch (SQLException sqle)
        {
            System.err.println("No se pudo conectar a BD");
            System.exit(1);
        }
        catch (java.lang.InstantiationException sqlex)
        {
            System.err.println("Imposible Conectar");
            System.exit(1);
        }
        catch (Exception ex)
        {
            System.err.println("Imposible Conectar");
            System.exit(1);
        }
        
    } // Fin constructor
    
    // Getter de Connection
    public Connection getConexion()
    {
        return conexion;
    } // Fin de metodo
            
} // Fin de clase
