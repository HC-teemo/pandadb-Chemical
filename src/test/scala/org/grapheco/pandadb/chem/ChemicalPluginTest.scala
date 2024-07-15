package org.grapheco.pandadb.chem

import com.typesafe.scalalogging.LazyLogging
import org.grapheco.lynx.LynxResult
import org.grapheco.lynx.types.property.LynxString
import org.grapheco.lynx.types.structural.LynxPropertyKey
import org.grapheco.pandadb.GraphDataBaseBuilder
import org.grapheco.pandadb.facade.{GraphDatabaseService, PandaTransaction}
import org.junit.jupiter.api.{Assertions, BeforeAll, Test}
import sourcecode.File

import scala.language.implicitConversions;


class ChemicalPluginTest extends LazyLogging{
    val path = "./db"

    val db: GraphDatabaseService = GraphDataBaseBuilder.newEmbeddedDatabase(path)
    implicit def propertyName(string: String): LynxPropertyKey = LynxPropertyKey(string)

    def withCreation(): Unit = {
        // clear
        execute("MATCH (n) DELETE n")
        val empty = execute("MATCH (n) RETURN n")
        Assertions.assertEquals(0, empty.records().size)
        // create
        execute(
            """
              |CREATE (:Acid{name:'Hydrochloric acid', name_zh:'盐酸', molecule: Chem.newMolecule('HCl')})
              |CREATE (:Acid{name:'Hypochlorous acid', name_zh:'次氯酸', molecule: Chem.newMolecule('HClO')})
              |CREATE (:Acid{name:'Chloric acid', name_zh:'氯酸', molecule: Chem.newMolecule('HClO3')})
              |CREATE (:Acid{name:'Perchloric acid', name_zh:'高氯酸', molecule: Chem.newMolecule('HClO4')})
              |CREATE (:Acid{name:'Sulfuric acid', name_zh:'硫酸', molecule: Chem.newMolecule('H2SO4')})
              |""".stripMargin)
    }

    def execute(statement: String): LynxResult = {
        val tx: PandaTransaction = db.beginTransaction()
        val result: LynxResult = tx.executeQuery(statement).cache()
        tx.commit()
        tx.close()
        result
    }

    @Test
    def de_encode(): Unit = {
        val water = new Molecule("H2O")
        val bytes = water.serialize()
        val decode = (new MoleculeFactory).fromBytes(bytes)
        Assertions.assertEquals(decode, water)
    }

    // UDF
    @Test
    def newAtomTest(): Unit = {
        val result = execute("RETURN Chem.newAtom('H') as atom")
        val atom = result.records().toList.head.get("atom")
        Assertions.assertTrue(atom.nonEmpty)
        Assertions.assertTrue(atom.forall(_.isInstanceOf[Atom]))
        Assertions.assertEquals(new Atom("H"), atom.get)
    }

    @Test
    def newMoleculeTest(): Unit = {
        val result = execute("RETURN Chem.newMolecule('H2O') as molecule")
        result.show()
        val molecule = result.records().toList.head.get("molecule")
        Assertions.assertTrue(molecule.nonEmpty)
        Assertions.assertTrue(molecule.forall(_.isInstanceOf[Molecule]))
        Assertions.assertEquals(new Molecule("H2O"), molecule.get)
    }

    // storage
    @Test
    def storageTest(): Unit = {
        withCreation()
        val result = execute("MATCH (n) RETURN n ORDER BY n.name")
        result.show()
        val acids = result.records().flatMap(_.getAsNode("n")).toList
        Assertions.assertEquals(5, acids.size)
        val ca = result.records().next().getAsNode("n").get
        Assertions.assertEquals(LynxString("Chloric acid"), ca.property("name").get)
        Assertions.assertEquals(LynxString("氯酸"), ca.property("name_zh").get)
        // new type property
        Assertions.assertEquals(new Molecule("HClO3"), ca.property("molecule").get)
    }

    @Test
    def subPropertyTest(): Unit = {
        withCreation()
        val result = execute("MATCH (n:Acid) RETURN n.molecule, n.molecule.atoms")
        result.show()
    }

    @Test
    def matchFilterTest(): Unit = {
        withCreation()
        val single = execute("MATCH (a:Acid{molecule: Chem.newMolecule('HCl')}) RETURN a.name, a.molecule")
        single.show()
        Assertions.assertEquals(1, single.records().size)
        val hcl = single.records().next()
        Assertions.assertEquals(LynxString("Hydrochloric acid"), hcl.getAsString("a.name").get)
        Assertions.assertEquals(new Molecule("HCl"), hcl.get("a.molecule").get)
    }

    @Test
    def matchFilterTest_multi(): Unit = {
        // Query all acid with Cl atom
        val multi = execute(
            """MATCH (a:Acid)
              |WHERE  Chem.newAtom('Cl') IN a.molecule.atoms
              |RETURN a""".stripMargin)
        multi.show()
        Assertions.assertEquals(4, multi.records().size)
    }

    @Test
    def orderByTest(): Unit = {
        // Query all acid with Cl atom, order by the number of O atom
        val result = execute(
            """
              |MATCH (a:Acid)
              |WHERE  Chem.newAtom('Cl') IN a.molecule.atoms
              |RETURN a, count(a.molecule.atoms) as atomCount
              |ORDER BY atomCount
              |""".stripMargin)
        result.show()
    }

    @Test
    def mergeTest(): Unit = {
        val result = execute(
            """
              |MATCH (a:Acid)
              |UNWIND a.molecule.atoms as atom
              |MERGE (a)-[:has_atoms]->(:Atom{atom: atom})
              |""".stripMargin)
        execute(
            """
              |MATCH (acid)-[r:has_atoms]->(atom)
              |RETURN acid,r,atom
              |""".stripMargin)
    }

    @Test
    def operatorTest(): Unit = {
        val result = execute(
            """
              |
              |""".stripMargin
        )
    }

    @Test
    def operatorAsFilter(): Unit = {
        val result = execute("")
    }
}