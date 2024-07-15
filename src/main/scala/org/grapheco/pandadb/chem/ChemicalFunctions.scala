package org.grapheco.pandadb.chem

import org.grapheco.lynx.func.LynxProcedure
import org.grapheco.lynx.types.LynxValue
import org.grapheco.lynx.types.property.{LynxInteger, LynxString}
import org.grapheco.pandadb.plugin.typesystem.TypeFunctions

import scala.util.Random

class ChemicalFunctions extends TypeFunctions{
  @LynxProcedure(name = "Chem.newAtom")
  def newAtom(inputs: LynxString): Atom = Atom.apply(inputs.v)

  @LynxProcedure(name = "Chem.newMolecule")
  def newMolecule(inputs: LynxString): Molecule = new Molecule(inputs.v)

}
