// SPDX-License-Identifier: Apache-2.0
// SPDX-FileCopyrightText: 2024 Jiuyang Liu <liu@jiuyang.me>
package oscc.dwbb.testbench.DW01_add

import chisel3.experimental.util.SerializableModuleElaborator
import mainargs.{arg, main, ParserForClass, ParserForMethods, TokensReader}
import org.chipsalliance.dwbb.wrapper.DW01_add.{DW01_add => Wrapper}
import org.chipsalliance.dwbb.interface.DW01_add.{Interface, Parameter}
import oscc.dwbb._
import oscc.dwbb.reference.{DW01_add => Reference}
import oscc.dwbb.testbench._

class Testbench(parameter: Parameter) extends TestBench[Parameter, Interface, Reference, Wrapper](parameter)

object Main extends SerializableModuleElaborator {
  implicit object PathRead extends TokensReader.Simple[os.Path] {
    def shortName = "path"
    def read(strs: Seq[String]) = Right(os.Path(strs.head, os.pwd))
  }

  @main
  case class ParameterMain(width: Int) {
    def convert: Parameter = Parameter(width)
  }
  implicit def parameterR: ParserForClass[ParameterMain] =
    ParserForClass[ParameterMain]

  @main
  def config(
    @arg(name = "parameter") parameter:  ParameterMain,
    @arg(name = "target-dir") targetDir: os.Path = os.pwd
  ) =
    os.write.over(targetDir / s"${packageName[Parameter]}.json", configImpl(parameter.convert))

  @main
  def design(
    @arg(name = "parameter") parameter:  os.Path,
    @arg(name = "target-dir") targetDir: os.Path = os.pwd
  ) = {
    val (firrtl, annos) =
      designImpl[Testbench, Parameter](os.read.stream(parameter))
    os.write.over(targetDir / s"${packageName[Parameter]}.fir", firrtl)
    os.write.over(targetDir / s"${packageName[Parameter]}.anno.json", annos)
  }

  def main(args: Array[String]): Unit = ParserForMethods(this).runOrExit(args)
}
