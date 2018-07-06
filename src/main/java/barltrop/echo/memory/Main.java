/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package barltrop.echo.memory;

import java.io.File;
import java.util.Map;
import org.neo4j.graphdb.*;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;

/**
 *
 * @author kevin
 */




public class Main {
    
    public enum NodeType implements Label{
        Person, Course;
    }
    
    public enum RelationType implements RelationshipType{
        IsChildOf;
    }
    
    public static void main(String[] args) {
        Listener listener = new Listener();
        Talker talker = new Talker();
        
        GraphDatabaseFactory dbFactory = new GraphDatabaseFactory();
        GraphDatabaseService graphDb;
        graphDb = dbFactory.newEmbeddedDatabase(
                new File("C:/Users/stuar/Documents/Neo4j","default.graphdb"));
        
       
        try(Transaction tx = graphDb.beginTx()){

            graphDb.execute( "MATCH (n) OPTIONAL MATCH (n)-[r]-() DELETE n,r");


            Node bobNode = graphDb.createNode(NodeType.Person);
            bobNode.setProperty("PId", 5001);
            bobNode.setProperty("Name", "Bob");
            bobNode.setProperty("Age", 23);
            
            Node bNode = graphDb.createNode(NodeType.Person);
            bNode.setProperty("PId", 5002);
            bNode.setProperty("Name", "John");
            bNode.setProperty("Age", 50);
            
            Relationship bobRelJohn = bobNode.createRelationshipTo( bNode, RelationType.IsChildOf);
            bobRelJohn.setProperty("Child","Son");

            
            tx.success();
            
            Result result = graphDb.execute( "MATCH (a) return a, a.Name");
            String rows = "";
            while (result.hasNext())
            {
                Map<String,Object> row = result.next();
                rows = row.entrySet().stream().map((column) -> column.getKey() + ": " + column.getValue( ) + "; ").reduce(rows, String::concat);
                rows += "\n";
            }
            System.out.print( rows);
        }
        graphDb.shutdown();
    }
    
    
}

