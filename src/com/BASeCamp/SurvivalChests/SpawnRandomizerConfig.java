package com.BASeCamp.SurvivalChests;
import java.io.File;
import java.util.HashMap;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.w3c.dom.*;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException; 
public class SpawnRandomizerConfig {
	
	
	
	
	public HashMap<EntityType,EntitySpawnData> SpawnData=new HashMap<EntityType,EntitySpawnData>();
	
/*
 <spawndata>
 <entity="Zombie" weight="Number">
 <weapon>
 <randomdata initializer="same as cfg line" weight="number"><randomdata>
 ... 
 </weapon>
[!--same story for <helmet>,<chestplate>, <leggings> and <boots>
 </entity>
  </spawndata>
  
  
 */
	public void randomizeEntity(LivingEntity Randomizethis){
		
		//
		//first create an array of EntitySpawnData and a corresponding array of floats for the probabilities.
		
		EntitySpawnData[] selectfrom = new EntitySpawnData[SpawnData.size()];
		float[] probabilities = new float[selectfrom.length];
		
		int i=0;
		for(EntityType iteratetype:SpawnData.keySet()){
			
			selectfrom[i] = SpawnData.get(iteratetype);
			probabilities[i] = selectfrom[i].getProbabilityWeight();
			i++;
			
		}
		
		EntitySpawnData chosen = RandomData.Choose(selectfrom,probabilities);
		
		//now, apply it.
		
		
		
		
	}
	public SpawnRandomizerConfig(File sourcefile){
		  DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
		  try {
          DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
          Document doc = docBuilder.parse (sourcefile);
      	  doc.getDocumentElement().normalize();
		  NodeList entitylist = doc.getElementsByTagName("entity");
		  int totalentities = entitylist.getLength();
          System.out.println("Total no of entities: " + totalentities);

          for(int s=0; s<entitylist.getLength() ; s++){


              Node entityNode = entitylist.item(s);
              if(entityNode.getNodeType() == Node.ELEMENT_NODE){
            	  if(entityNode instanceof Element){

                  Element EntityElement = (Element)entityNode;

                  //we are looking at an entity element.
                  //we need to acquire the relevant information and construct a new EntitySpawnData.
                  EntitySpawnData esd = new EntitySpawnData(EntityElement);
                  
                  SpawnData.put(esd.getSpawnEntity(),esd);
            	  }

              }//end of if clause


          }//end of for loop with s var
		  
		  
		  }
		  catch(Exception exx){
			  
			  System.out.println("Exception in SpawnRandomizerConfig:" + exx.toString());
			  
		  }
		
	
	}
	
	
	
}
