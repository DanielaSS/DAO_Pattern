/*
 * Copyright (C) 2016 hcadavid
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

import java.sql.PreparedStatement;
import edu.eci.pdsw.samples.entities.Consulta;
import edu.eci.pdsw.samples.entities.Paciente;
import edu.eci.pdsw.samples.persistence.DaoFactory;
import edu.eci.pdsw.samples.persistence.DaoPaciente;
import edu.eci.pdsw.samples.persistence.PersistenceException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author hcadavid
 */
public class PacientePersistenceTest {
    //0
    @Test
    public void databaseConnectionTest() throws IOException, PersistenceException{
        System.out.println("Prueba 0");
        InputStream input = null;
        input = ClassLoader.getSystemResourceAsStream("applicationconfig_test.properties");
        Properties properties=new Properties();
        properties.load(input);
        
        DaoFactory daof=DaoFactory.getInstance(properties);
        
        daof.beginSession();
        
        //IMPLEMENTACION DE LAS PRUEBAS
        //fail("Pruebas no implementadas");
            

        daof.commitTransaction();
        daof.endSession();        
    }
    //1         DAOPaciente.save()      Paciente nuevo que se registra con mas de una consulta
    @Test
    public void classEquivRegistroPacienteMasDeUnaConsulta(){
        System.out.println("Prueba 1");
        InputStream input = null;
        input = ClassLoader.getSystemResourceAsStream("applicationconfig_test.properties");
        Properties properties=new Properties();
        try {
            properties.load(input);
        } catch (IOException ex) {
            fail("No se cargaron las propiedades");
        }
            
        DaoFactory daof=DaoFactory.getInstance(properties);
        try {

            
            daof.beginSession();
            
            String query="select pac.nombre, pac.fecha_nacimiento, con.idCONSULTAS, con.fecha_y_hora, con.resumen "
                    + "from PACIENTES as pac inner join CONSULTAS as con on con.PACIENTES_id=pac.id "
                    + "and con.PACIENTES_tipo_id=pac.tipo_id where pac.id=? and pac.tipo_id=?";
            //IMPLEMENTACION DE LAS PRUEBAS
            DaoPaciente a=daof.getDaoPaciente();
            Paciente paciente=new Paciente(2,"CC","Martin no subir",Date.valueOf("2005-08-15"));
            Consulta unaConsulta=new Consulta(Date.valueOf("2016-03-03"),"Martin lo subio");
            Consulta otraConsulta=new Consulta(Date.valueOf("2015-10-10"),"Ahora carlos lo subio");
            Set<Consulta> setConsultas=new HashSet<Consulta>();
            setConsultas.add(unaConsulta);
            setConsultas.add(otraConsulta);
            paciente.setConsultas(setConsultas);
         
            a.save(paciente);
            
            daof.commitTransaction();
            Connection cone=a.getCon();
            PreparedStatement ps=cone.prepareStatement(query);
            ps.setInt(1, 2);
            ps.setString(2, "CC");
            ResultSet rs=ps.executeQuery();
            boolean bandera=rs.next();
            if(!bandera){
                fail("No retorno nada la consulta");
            }
            while(bandera){
                assertEquals("Martin no subir", rs.getString(1));
                assertEquals(Date.valueOf("2005-08-15"), rs.getDate(2));
                assertTrue(rs.getString(5).equals("Martin lo subio")||rs.getString(5).equals("Ahora carlos lo subio"));
                bandera=rs.next();
            }
            daof.endSession();
        } catch (Exception ex) {
            try {
                daof.endSession();
                fail("Lanzo excepcion: "+ex.getMessage());
            } catch (PersistenceException ex1) {
                fail("Error al cerrar");
            }
        }         
    }
    //2         DAOPaciente.save()      Paciente nuevo que se registra sin consultas
    @Test
    public void classEquivRegistroPacienteSinConsultas(){
        System.out.println("Prueba 2");
        InputStream input = null;
        input = ClassLoader.getSystemResourceAsStream("applicationconfig_test.properties");
        Properties properties=new Properties();
        try {
            properties.load(input);
        } catch (IOException ex) {
            fail("No se cargaron las propiedades");
        }
            
        DaoFactory daof=DaoFactory.getInstance(properties);
        try {

            String query="select pac.nombre, pac.fecha_nacimiento, con.idCONSULTAS, con.fecha_y_hora, con.resumen "
                    + "from PACIENTES as pac inner join CONSULTAS as con on con.PACIENTES_id=pac.id "
                    + "and con.PACIENTES_tipo_id=pac.tipo_id where pac.id=? and pac.tipo_id=?";
            daof.beginSession();
            
            //IMPLEMENTACION DE LAS PRUEBAS
            DaoPaciente a=daof.getDaoPaciente();
            Paciente paciente=new Paciente(4,"CE","Casvad",Date.valueOf("2000-08-15"));

            a.save(paciente);
            
            daof.commitTransaction(); 
            Connection cone=a.getCon();
            PreparedStatement ps=cone.prepareStatement(query);
            ps.setInt(1, 4);
            ps.setString(2, "CE");
            ResultSet rs=ps.executeQuery();
            boolean bandera=rs.next();
            if(!bandera){
                fail("No retorno nada la consulta");
            }
            else{
                assertEquals("Casvad", rs.getString(1));
                assertEquals(Date.valueOf("2005-08-15"), rs.getDate(2));
            }            
            daof.endSession();
        } catch (Exception ex) {
            try {
                daof.endSession();
                fail("Lanzo excepcion: "+ex.getMessage());
            } catch (PersistenceException ex1) {
                System.out.println("Error al cerrar");
                fail();
            }
        }   
    }
    //3 	DAOPaciente.save() 	Paciente nuevo que se registra con sólo una consulta 	
    //Dani, hay que modificar la prueba al estilo de las dos primeras, de lo contrario queda sin cerrar
    //Tambien tienes que hacer una consulta SQL
   @Test
    public void classEquivSaveNuevoPacienteConUnaConsulta(){
        try {
            InputStream input = null;
            input = ClassLoader.getSystemResourceAsStream("applicationconfig_test.properties");
            Properties properties=new Properties();
            properties.load(input);
            
            DaoFactory daof=DaoFactory.getInstance(properties);
            
            daof.beginSession();
            
            //IMPLEMENTACION DE LAS PRUEBAS
            DaoPaciente a=daof.getDaoPaciente();
            Paciente paciente=new Paciente(1,"CC","Isabel Marin",Date.valueOf("2013-08-15"));
            Consulta unaConsulta=new Consulta(Date.valueOf("2016-03-03"),"Isa se desmayo");
            Set<Consulta> setConsultas=new HashSet<Consulta>();
            setConsultas.add(unaConsulta);
            paciente.setConsultas(setConsultas);
            a.save(paciente);
            
            daof.commitTransaction(); 
            daof.endSession();
        } catch (IOException|PersistenceException ex) {
            
            fail("Lanzo excepcion: "+ex.getMessage());
        }

    }
    //4 	DAOPaciente.save() 	Paciente nuevo YA existente que se registra con más de una consulta
    @Test
    public void classEquivSaveNuevoPacient(){
    
    }
    
   
}
