package fr.paris8.iut.info.stare;
/*
 * $Id$
 *
 * Copyright (C) Paris8-IUT de Montreuil, 2013-2017
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation; either version 2.1 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307 USA
 *
 */
  
import java.io.Serializable;
/*import java.util.HashMap;
import java.util.Set;
import java.util.Map;
*/
import fr.paris8.iut.info.stare.OntologyData;
import fr.paris8.iut.info.stare.Structure;
import fr.paris8.iut.info.stare.Frame;

//The platform sends an object of this class to a client.
//When each client receives such an object
public class StareAgent implements Serializable 
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	//found=null if "data" is not decided
	private Boolean found = null;
	
	private OntologyData oData;
	private Structure rData;
	
	public StareAgent(OntologyData data) 
	{
		oData = data;
		rData = new Structure();
	}
	 
	//It fills boolean  "found"
	public boolean process( ) 
	{
		Frame frame = new Frame(); 
        frame.buildInParallelization(oData, rData);
        return frame.getConsistent();       
	}

	public Boolean getFound() {
		return found;
	}

	
	public OntologyData getOntologyData(){
		return this.oData;
	}
    
}