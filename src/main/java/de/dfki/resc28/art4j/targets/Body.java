/*
 * This file is part of art4j. It is subject to the license terms in
 * the LICENSE file found in the top-level directory of this distribution.
 * You may not use this file except in compliance with the License.
 */

package de.dfki.resc28.art4j.targets;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.vocabulary.RDF;

import de.dfki.resc28.art4j.vocabularies.ART;
import de.dfki.resc28.art4j.vocabularies.MATHS;
import de.dfki.resc28.art4j.vocabularies.SPATIAL;
import de.dfki.resc28.art4j.vocabularies.VOM;


public class Body extends Target6DOF
{
	public Body(final int id, final double quality, final double[] location, final double[] rotation)
	{
		super(id, quality, location, rotation);
	}
	

	
	public Model getRDF(String bodyContainerURI)
	{
		Model bodyModel = ModelFactory.createDefaultModel();
		
		bodyModel.setNsPrefixes(ART.NAMESPACE);
		bodyModel.setNsPrefixes(SPATIAL.NAMESPACE);
		bodyModel.setNsPrefixes(VOM.NAMESPACE);
		bodyModel.setNsPrefixes(MATHS.NAMESPACE);
		
		Resource body = bodyModel.createResource(bodyContainerURI + "/" + this.id);
		Resource sr = bodyModel.createResource();
		Resource sourceCS = bodyModel.createResource();
		Resource targetCS = bodyModel.createResource();
		Resource translation = bodyModel.createResource();
		Resource vec3 = bodyModel.createResource();
		Resource rotation = bodyModel.createResource();
		Resource mat3 = bodyModel.createResource();
		
		bodyModel.add(sourceCS, RDF.type, MATHS.CoordinateSystem);
		bodyModel.add(targetCS, RDF.type, MATHS.CoordinateSystem);
		
		bodyModel.add(body, RDF.type, ART.Target);
		bodyModel.add(body, SPATIAL.spatialRelationship, sr);
		
		bodyModel.add(sr, RDF.type,  SPATIAL.SpatialRelationship);
		bodyModel.add(sr, SPATIAL.sourceCoordinateSystem, sourceCS);
		bodyModel.add(sr, SPATIAL.targetCoordinateSystem, targetCS);
		bodyModel.add(sr, SPATIAL.translation, translation);
		bodyModel.add(sr, SPATIAL.rotation, rotation);
		
		bodyModel.add(translation, RDF.type, SPATIAL.Translation3D);
		bodyModel.add(translation, VOM.quantityValue, vec3);

		bodyModel.add(vec3, RDF.type, MATHS.Vector3D);
		bodyModel.add(vec3, MATHS.x, bodyModel.createTypedLiteral(this.location[0]));
		bodyModel.add(vec3, MATHS.y, bodyModel.createTypedLiteral(this.location[1]));
		bodyModel.add(vec3, MATHS.z, bodyModel.createTypedLiteral(this.location[2]));
		
		bodyModel.add(rotation, RDF.type, SPATIAL.Rotation3D);
		bodyModel.add(rotation, VOM.quantityValue, mat3);
		
		bodyModel.add(mat3, RDF.type, MATHS.Matrix3D);
		bodyModel.add(mat3, MATHS.a11, bodyModel.createTypedLiteral(this.rotation[0]));
		bodyModel.add(mat3, MATHS.a12, bodyModel.createTypedLiteral(this.rotation[1]));
		bodyModel.add(mat3, MATHS.a13, bodyModel.createTypedLiteral(this.rotation[2]));
		bodyModel.add(mat3, MATHS.a21, bodyModel.createTypedLiteral(this.rotation[3]));
		bodyModel.add(mat3, MATHS.a22, bodyModel.createTypedLiteral(this.rotation[4]));
		bodyModel.add(mat3, MATHS.a23, bodyModel.createTypedLiteral(this.rotation[5]));
		bodyModel.add(mat3, MATHS.a31, bodyModel.createTypedLiteral(this.rotation[6]));
		bodyModel.add(mat3, MATHS.a32, bodyModel.createTypedLiteral(this.rotation[7]));
		bodyModel.add(mat3, MATHS.a33, bodyModel.createTypedLiteral(this.rotation[8]));
		
		return bodyModel;
	}
	
	public void printLocation()
	{
		System.out.println(getLocation()[0] + ", " + getLocation()[1] + ", " + getLocation()[2]);
	}
	
	
	public void printRotation()
	{
		System.out.println(getRotation()[0] + ", " + getRotation()[1] + ", " + getRotation()[2]);
	}
}