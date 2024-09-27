# SPDX-License-Identifier: Apache-2.0
# SPDX-FileCopyrightText: 2024 Jiuyang Liu <liu@jiuyang.me>

{ stdenvNoCC
, rtl
, cds-fhs-env
, dwbb-ip
}:

stdenvNoCC.mkDerivation {
  name = "jg-fpv";

  # Add "sandbox = relaxed" into /etc/nix/nix.conf, and run `systemctl restart nix-daemon`
  #
  # run nix build with "--impure" flag to build this package without chroot
  # require license
  __noChroot = true;
  dontPatchELF = true;

  src = rtl;

  passthru = {
    inherit rtl;
  };

  shellHook = ''
    echo "[nix] entering fhs env"
    ${cds-fhs-env}/bin/cds-fhs-env
  '';

  buildPhase = ''
    runHook preBuild

    cp ${dwbb-ip}/sim_ver/${rtl.target}.v .
    echo ${rtl.target}.v >> filelist.f

    echo "[nix] running Jasper"
    fhsBash=${cds-fhs-env}/bin/cds-fhs-env
    "$fhsBash" -c "jg -batch ${./scripts/FPV.tcl}"
    
    runHook postBuild
  '';

  installPhase = ''
    runHook preInstall
    
    mkdir -p $out
    
    cp report.txt $out
    cp -r jgproject $out

    runHook postInstall
  '';

}
