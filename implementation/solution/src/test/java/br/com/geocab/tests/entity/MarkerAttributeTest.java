/**
 * 
 */
package br.com.geocab.tests.entity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import br.com.geocab.domain.entity.layer.Attribute;
import br.com.geocab.domain.entity.marker.Marker;
import br.com.geocab.domain.entity.marker.MarkerAttribute;

/**
 * @author emanuelvictor
 *
 */
public class MarkerAttributeTest
{
	/*-------------------------------------------------------------------
	 *				 		     ATTRIBUTES
	 *-------------------------------------------------------------------*/

	/*-------------------------------------------------------------------
	 *				 		     	TESTS
	 *-------------------------------------------------------------------*/

	/**
	 * Verifica validação para não duplicar o nome dos atributos dentro do marker para o shapefile
	 */
	@Test
	public void testMarkerAttributeHandlerNoDuplicateAttributes()
	{
		Attribute attribute1 = new Attribute();
		attribute1.setName("Nome da foto");
				
		Attribute attribute2 = new Attribute();
		attribute2.setName("Nome da foto");
		
		Attribute attribute3 = new Attribute();
		attribute3.setName("Nome da foto");
		
		Attribute attribute4 = new Attribute();
		attribute4.setName("Nome da foto");
		
		Attribute attribute5 = new Attribute();
		attribute5.setName("Outro Nome da foto");
		
		Attribute attribute6 = new Attribute();
		attribute6.setName("Outro Nome da foto");
		
		Marker marker = new Marker();
		
		MarkerAttribute markerAttribute1 = new MarkerAttribute();
		markerAttribute1.setAttribute(attribute1);
		
		MarkerAttribute markerAttribute2 = new MarkerAttribute();
		markerAttribute2.setAttribute(attribute2);
		
		MarkerAttribute markerAttribute3 = new MarkerAttribute();
		markerAttribute3.setAttribute(attribute3);
		
		MarkerAttribute markerAttribute4 = new MarkerAttribute();
		markerAttribute4.setAttribute(attribute4);
		
		MarkerAttribute markerAttribute5 = new MarkerAttribute();
		markerAttribute5.setAttribute(attribute5);
		
		MarkerAttribute markerAttribute6 = new MarkerAttribute();
		markerAttribute6.setAttribute(attribute6);
		
		marker.setMarkerAttribute(Arrays.asList(markerAttribute1, markerAttribute2, markerAttribute3, markerAttribute4, markerAttribute5, markerAttribute6));
				
		List<MarkerAttribute> markerAttributes = new ArrayList<>();
		 
		for (MarkerAttribute markerAttribute : marker.getMarkerAttribute())
		{
			MarkerAttribute ma= new MarkerAttribute();
			ma.setAttribute(new Attribute(markerAttribute.getAttribute().getName()));
			markerAttributes.add(ma);
		}
		marker.handlerDuplicateAttributes();
		System.out.println("-------ARRAY DE SAÍDA -------");
		for (MarkerAttribute markerAttribute : marker.getMarkerAttribute())
		{
			System.out.println(markerAttribute.getAttribute().getName());
		}
		System.out.println("-------ARRAY DE DESTINO -------");
		for (MarkerAttribute markerAttribute : markerAttributes)
		{
			System.out.println(markerAttribute.getAttribute().getName());
		}
		
		//TODO encontrar forma de comparar valores de COLLECTIONST CollectionUtils.isEqualCollection(markerAttributes, marker.getMarkerAttribute());
		
	}

	/**
	 * Verifica validação para não duplicar o nome dos atributos dentro do marker para o shapefile
	 * Verifica formatação dos atributos para shapefile
	 */
	@Test
	public void testMarkerAttributeFormattAttributes()
	{
		Attribute attribute1 = new Attribute();
		attribute1.setName("Nome da foto");
				
		Attribute attribute2 = new Attribute();
		attribute2.setName("Nome da foto");
		
		Attribute attribute3 = new Attribute();
		attribute3.setName("Nome da foto");
		
		Attribute attribute4 = new Attribute();
		attribute4.setName("Nome da foto");
		
		Attribute attribute5 = new Attribute();
		attribute5.setName("Outro Nome da foto");
		
		Attribute attribute6 = new Attribute();
		attribute6.setName("Outro Nome da foto");
		
		Marker marker = new Marker();
		
		MarkerAttribute markerAttribute1 = new MarkerAttribute();
		markerAttribute1.setAttribute(attribute1);
		
		MarkerAttribute markerAttribute2 = new MarkerAttribute();
		markerAttribute2.setAttribute(attribute2);
		
		MarkerAttribute markerAttribute3 = new MarkerAttribute();
		markerAttribute3.setAttribute(attribute3);
		
		MarkerAttribute markerAttribute4 = new MarkerAttribute();
		markerAttribute4.setAttribute(attribute4);
		
		MarkerAttribute markerAttribute5 = new MarkerAttribute();
		markerAttribute5.setAttribute(attribute5);
		
		MarkerAttribute markerAttribute6 = new MarkerAttribute();
		markerAttribute6.setAttribute(attribute6);
		
		marker.setMarkerAttribute(Arrays.asList(markerAttribute1, markerAttribute2, markerAttribute3, markerAttribute4, markerAttribute5, markerAttribute6));
				
		List<MarkerAttribute> markerAttributes = new ArrayList<>();
		 
		for (MarkerAttribute markerAttribute : marker.getMarkerAttribute())
		{
			MarkerAttribute ma= new MarkerAttribute();
			ma.setAttribute(new Attribute(markerAttribute.getAttribute().getName()));
			markerAttributes.add(ma);
		}
		marker.formattedAttributes();
		marker.handlerDuplicateAttributes();
		System.out.println("-------ARRAY DE SAÍDA -------");
		for (MarkerAttribute markerAttribute : marker.getMarkerAttribute())
		{
			System.out.println(markerAttribute.getAttribute().getName());
		}
		System.out.println("-------ARRAY DE DESTINO -------");
		for (MarkerAttribute markerAttribute : markerAttributes)
		{
			System.out.println(markerAttribute.getAttribute().getName());
		}
		
		//TODO encontrar forma de comparar valores de COLLECTIONST CollectionUtils.isEqualCollection(markerAttributes, marker.getMarkerAttribute());
		
	}
	
}
