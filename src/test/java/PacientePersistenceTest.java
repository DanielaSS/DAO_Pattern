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
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author hcadavid
 */
public class PacientePersistenceTest {
    //1         DAOPaciente.save()      Paciente nuevo que se registra con mas de una consulta
    @Test
    public void classEquivRegistroPacienteMasDeUnaConsulta(){
        System.out.println("Prueba 1 Paciente mas de una consulta");
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
            daof.endSession();
            if(!bandera){
                fail(PersistenceException.PACIENTE_NO_EXISTENTE);
            }
            while(bandera){
                assertTrue(paciente.getNombre().equals(rs.getString(1)) && paciente.getFechaNacimiento().equals(rs.getDate(2))&& (rs.getString(5).equals("Martin lo subio")||rs.getString(5).equals("Ahora carlos lo subio")));
                bandera=rs.next();
            }
            
        } catch (PersistenceException | SQLException ex) {
           /* try {
                daof.endSession();
                fail("Lanzo excepcion: "+ex.getMessage());
            } catch (PersistenceException ex1) {
                fail("Error al cerrar"+ex1.getMessage());
            }*/
        }         
    }
    //2         DAOPaciente.save()      Paciente nuevo que se registra sin consultas
    @Test
    public void classEquivRegistroPacienteSinConsultas(){
        System.out.println("Prueba 2 Paciente sin consultas");
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
            daof.endSession();
            if(!bandera){
                fail("No retorno nada la consulta");
            }
            else{
                assertTrue(rs.getString(1).equals("Casvad") && rs.getDate(2).equals(Date.valueOf("2005-08-15")));
            }            
            
        } catch (PersistenceException | SQLException ex) {
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
   @Test
    public void classEquivSaveNuevoPacienteConUnaConsulta(){
        System.out.println("Prueba 3, paciente nuevo que se registra con solo una consulta");
        InputStream input = null;
        input = ClassLoader.getSystemResourceAsStream("applicationconfig_test.properties");
        Properties properties=new Properties();       
        try {
            properties.load(input);
        } catch (IOException ex) {
            fail("No se cargaron las propiedades");
        }
        DaoFactory daof=DaoFactory.getInstance(properties);
        try{
            daof.beginSession();
            DaoPaciente persistenciaPaciente=daof.getDaoPaciente();
            Paciente unPaciente=new Paciente(12,"CC","Isabel Marin",Date.valueOf("1990-08-15"));
            Consulta unaConsulta=new Consulta(Date.valueOf("2016-03-03"),"Golpe en la cabeza por desmayo");
            Set<Consulta> setConsultas=new HashSet<Consulta>();
            setConsultas.add(unaConsulta);
            unPaciente.setConsultas(setConsultas);
            persistenciaPaciente.save(unPaciente);
            daof.commitTransaction();
            
            //Consultamos al paciente
            Connection cone=persistenciaPaciente.getCon();
            String query="select pac.nombre, pac.fecha_nacimiento, con.idCONSULTAS, con.fecha_y_hora, con.resumen "
                    + "from PACIENTES as pac inner join CONSULTAS as con on con.PACIENTES_id=pac.id "
                    + "and con.PACIENTES_tipo_id=pac.tipo_id where pac.id=? and pac.tipo_id=?";
            PreparedStatement ps=cone.prepareStatement(query);
       
            //Enviamos datos a la consulta
            ps.setInt(1, 12);
            ps.setString(2, "CC");
            ResultSet rs=ps.executeQuery();
            
            if(!rs.next()){
                fail(PersistenceException.PACIENTE_NO_EXISTENTE);
            }else{
                
                assertTrue("No fueron equivalentes",rs.getString(5).equals(unaConsulta.getResumen()) && rs.getDate(4).equals(unaConsulta.getFechayHora()));
            }
            daof.endSession();
        } catch (SQLException | PersistenceException ex) {
            try {
                daof.endSession();
                fail("Lanzo excepcion: "+ex.getMessage());
            } catch (PersistenceException ex1) {
                fail(ex1.getMessage()+"Error al cerrar");
            }
        }  
    }
    //4 	DAOPaciente.save() 	Paciente nuevo YA existente que se registra con más de una consulta
    @Test
    public void classEquivPacienteRepetido(){
        System.out.println("Prueba 4, paciente nuevo YA existente que se registra con más de una consulta");
        InputStream input = null;
        input = ClassLoader.getSystemResourceAsStream("applicationconfig_test.properties");
        Properties properties=new Properties();       
        try {
            properties.load(input);
        } catch (IOException ex) {
            fail("No se cargaron las propiedades");
        }
        DaoFactory daof=DaoFactory.getInstance(properties);
        try{
            daof.beginSession();
            DaoPaciente persistenciaPaciente=daof.getDaoPaciente();
            Paciente unPaciente=new Paciente(5,"CC","Maria alejandra Gallego",Date.valueOf("1999-01-30"));
            //System.out.println("Paso");
            persistenciaPaciente.save(unPaciente);
            //System.out.println("Paso");
            Consulta unaConsulta=new Consulta(Date.valueOf("2016-01-26"),"Alergia a picadura de abeja");
            Consulta dosConsulta=new Consulta(Date.valueOf("2016-01-27"),"Revision picadura abeja");
            Consulta tresConsulta=new Consulta(Date.valueOf("2016-02-21"),"Revision efecto de los antinflamatorios");
            Set<Consulta> setConsultas=new HashSet<Consulta>();
            setConsultas.add(unaConsulta);
            setConsultas.add(dosConsulta);
            setConsultas.add(tresConsulta);
            unPaciente.setConsultas(setConsultas);
            //System.out.println("Entra 2");
            persistenciaPaciente.save(unPaciente);          
            fail("No lanzo excepcion");
        } catch (PersistenceException ex) {
            try {
                daof.endSession();
            } catch (PersistenceException ex1) {
                fail("Error al cerrar");
            }
            //System.out.println(ex);
            assertEquals(ex.getMessage(), PersistenceException.PACIENTE_EXISTENTE);
        } 
    }

}
