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

import java.io.File;
/*
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.Map;*/
import java.util.ArrayList;
import java.util.Collection;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.io.OWLOntologyCreationIOException;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.OWLException;
import org.semanticweb.owlapi.model.OWLIndividual;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLPropertyExpression;

import fr.paris8.iut.info.stare.OntologyData;
import fr.paris8.iut.info.stare.Structure;
import fr.paris8.iut.info.stare.LoadOntology;

//To be generalized as an interface
//The plaform instanciates this class with an ontology file (.owl)  and a number of clients (agents)

public class DistributedInterface 
{
	//To be generalized
	//found=null if "data" is not decided
	//The platform terminates when found = true
	private Boolean found = null;

	// This set is updated (adding) in on-the-fly way. It may be very large
	private Collection<OntologyData> data;

	//nb : the  number of agents
	public DistributedInterface( String ontoFile, int nbAgent ) 
	{
		data = new ArrayList<OntologyData>();
		setData( ontoFile, nbAgent );
	}

	// This method modualizes an ontology into a set of OntologyData
	// It adds modules (OntologyData) to "data" in on-the-fly way
	public void setData( String ontoFile, int nbAgent )
	{
		OWLOntologyManager manager = OWLManager.createOWLOntologyManager( );
		OWLOntology onto1 = null;
		try {
			onto1 = manager.loadOntology( IRI.create( new File(ontoFile)) ); 

		/*}catch (OWLOntologyCreationIOException e ){
			System.out.println("File not found");
			System.exit(0);*/
		} catch (OWLException ex) { 
			ex.printStackTrace();
		}
		LoadOntology onto = new LoadOntology( onto1 ); 	   
		OntologyData oData = onto.getData();
		for(int i=0;i<nbAgent;i++){
			data.add(oData);
		}
	}

	public Boolean getFound() {
		return found;
	}

	public Collection<OntologyData> getData() {
		return data;
	}


}