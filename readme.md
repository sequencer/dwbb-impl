# DWBB(DesignWare Building Block)

[The DesignWare Library's Datapath and Building Block IP](https://www.synopsys.com/dw/buildingblock.php) is a collection of reusable intellectual property blocks that are tightly integrated into the Synopsys synthesis environment.

This is its wrapper in Chisel. This project is designed for providing an interface for Chisel user to easily instantiate the DWBB IP to improve the circuit performance and reduce the duplicate library work.

**This project is not meant to replace the DWBB nor providing the opensource implementation to it, the design in reference folder is only meant for simulation usage. and won't grantee its performance**

## Project Structure

There are two folders for the project.

- reference

  Under the reference folder, is the opensource implementation to corresponding DWBB block. This is typically out of date due to lack to engineering resources.

- testbench
  
  the testbench folder contains a template(which can be implemented via macro or compiler plugin) to create a formal checker for blackbox and reference.

## User Guide

Import this project into your project via submodule. Project example will be given in the future.

## Contribution Guide

Add reference for target design:

```scala
// SPDX-License-Identifier: Apache-2.0
// SPDX-FileCopyrightText: 2024 Jiuyang Liu <liu@jiuyang.me>
package oscc.dwbb.reference

import chisel3._
import oscc.dwbb.interface._

class SOME_DWBB(parameter: SOME_DWBB.Parameter)
    extends ReferenceModule(new SOME_DWBB.Interface(parameter), parameter) {
  // Implement your hardware here.
}
```

Add elaborator for target design:

```bash
cd dwbb/src/testbench
sed 's/DW01_add/SOME_DWBB/g' DW01_add.scala > SOME_DWBB.scala
```

Generate proper configs as test case:

```bash
mkdir configs/SOME_DWBB
nix run .#dwbb.SOME_DWBB.noconfig.elaborator -- config --case-name case01 <other parameters>
nix run .#dwbb.SOME_DWBB.noconfig.elaborator -- config --case-name case02 <other parameters>
# and more...
```

Register the design in `nix/dwbb/default.nix`:

```diff
--- a/nix/dwbb/default.nix
+++ b/nix/dwbb/default.nix
@@ -39,5 +39,9 @@ in
  DW01_add = newDesign {
     target = "DW01_add";
     layers = [ "Verification.BMC" ];
   };
+  SOME_DWBB = newDesign {
+    target = "SOME_DWBB";
+    layers = [ "Verification.BMC" ];
+  };
 })
```

Then you can run the jasper with the name of the module that you have added as the target:

```bash
nix build '.#dwbb.SOME_DWBB.<testcase>.jg-fpv' --impure
```

and the report will be generated in the result/

- Note that in order to use jasper gold for formal verification, you need to set the environment variables `JASPER_HOME`,`CDS_LIC_FILE`, `DWBB_DIR` and add the`--impure` flag.

## License

The DesignWare Building Block is the property of Synopsys, Inc. All Rights Reserved.

The Hardware IP generators implemented in this library are under [Apache-2.0 License](https://opensource.org/licenses/Apache-2.0).

Copyright All Rights Reserved Jiuyang Liu <liu@jiuyang.me>
