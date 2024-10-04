# SPDX-License-Identifier: Apache-2.0
# SPDX-FileCopyrightText: 2024 Jiuyang Liu <liu@jiuyang.me>

{ lib, newScope, }:
lib.makeScope newScope (scope:
let
  newDesign = { target, layers ? [ "Verification.Assume" "Verification.Assert" ] }:
    let
      jsonList = builtins.attrNames (builtins.readDir ./../../configs/${target}) ++ [ "noconfig" ];
    in
    builtins.listToAttrs (builtins.map
      (case: {
        name = lib.strings.removeSuffix ".json" case;
        value = rec {
          compiled = scope.callPackage ./compiled.nix {
            target = target;
          };
          inherit (compiled) elaborator;
          elaborate = scope.callPackage ./elaborate.nix {
            elaborator = elaborator;
            caseJson = case;
          };
          mlirbc =
            scope.callPackage ./mlirbc.nix { elaborate = elaborate; };
          rtl = scope.callPackage ./rtl.nix {
            mlirbc = mlirbc;
            enable-layers = layers;
          };
          jg-fpv = scope.callPackage ./jg-fpv.nix {
            rtl = rtl;
          };
        };
      }
      )
      jsonList);
in
{
  DW01_add = newDesign {
    target = "DW01_add";
    layers = [ "Verification.Assume" "Verification.Assert" ];
  };
})

