#!/bin/sh
#
# Copyright (c) 2017 Red Hat, Inc.
# This program and the accompanying materials are made
# available under the terms of the Eclipse Public License 2.0
# which is available at https://www.eclipse.org/legal/epl-2.0/
#
# SPDX-License-Identifier: EPL-2.0
#

base_dir=$(cd "$(dirname "$0")"; pwd)
. ./build.include

# grab assembly
DIR=$(cd "$(dirname "$0")"; pwd)
if [ ! -d "${DIR}/assembly/target" ]; then
  echo "${ERROR}Have you built assembly in ${DIR}/assembly 'mvn clean install'?"
  exit 2
fi

# Use of folder
BUILD_ASSEMBLY_DIR=$(echo "${DIR}"/assembly/target/eclipse-che-*/eclipse-che-*/)
LOCAL_ASSEMBLY_DIR="${DIR}"/eclipse-che

if [ -d "${LOCAL_ASSEMBLY_DIR}" ]; then
  rm -r "${LOCAL_ASSEMBLY_DIR}"
fi

echo "Copying assembly ${BUILD_ASSEMBLY_DIR} --> ${LOCAL_ASSEMBLY_DIR}"
cp -r "${BUILD_ASSEMBLY_DIR}" "${LOCAL_ASSEMBLY_DIR}"

init --name:logs "$@"
build

#cleanUp
rm -rf ${DIR}/eclipse-che