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

import edu.eci.pdsw.samples.entities.Consulta;
import edu.eci.pdsw.samples.entities.Paciente;
import edu.eci.pdsw.samples.persistence.DaoFactory;
import edu.eci.pdsw.samples.persistence.DaoPaciente;
import edu.eci.pdsw.samples.persistence.PersistenceException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Date;
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
    
    public PacientePersistenceTest() {
    }
    
    @Before
    public void setUp() {
    }
    
    @Test
    public void databaseConnectionTest() throws IOException, PersistenceException{
        InputStream input = null;
        input = ClassLoader.getSystemResourceAsStream("applicationconfig_test.properties");
        Properties properties=new Properties();
        properties.load(input);
        
        DaoFactory daof=DaoFactory.getInstance(properties);
        
        daof.beginSession();
        
        //IMPLEMENTACION DE LAS PRUEBAS
        fail("Pruebas no implementadas");
            

        daof.commitTransaction();
        daof.endSession();        
    }
    //3 	DAOPaciente.save() 	Paciente nuevo que se registra con sólo una consulta 	
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
            //3 	DAOPaciente.save() 	Paciente nuevo que se registra con sólo una consulta
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
