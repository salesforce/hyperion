#! /usr/bin/env bash

usage() {
  echo "usage: gsutil_download.sh s3://bucket/boto.config gs://gsbucket/path/ true|false"
  echo
  echo "The contents of the GS path will be downloaded to OUTPUT1_STAGING_DIR (${OUTPUT1_STAGING_DIR})"
  exit 3
}

if ([ -z "$1" ] || [ -z "$2" ]); then
  echo "ERROR: missing arguments"
  usage
fi

if [ -z "${OUTPUT1_STAGING_DIR}" ]; then
  echo "ERROR: OUTPUT1_STAGING_DIR must be specified"
  usage
fi

declare -a on_exit_items

function on_exit() {
  for i in "${on_exit_items[@]}"; do
    eval ${i}
  done
}

function add_on_exit() {
  local n=${#on_exit_items[*]}
  on_exit_items[$n]="$*"
  if [[ ${n} -eq 0 ]]; then
    trap on_exit EXIT
  fi
}

set -xe

WORKING_DIR=$(mktemp -d)
cd ${WORKING_DIR}
add_on_exit rm -rf ${WORKING_DIR}

# This is the name of the tarball
TARBALL="gsutil.tar.gz"

# This is the download location for the latest gsutil release
GSUTIL_URL="https://storage.googleapis.com/pub/${TARBALL}"

# This is the configuration file for gsutil
export BOTO_CONFIG="boto.gsutil"

# This is the source of the boto config file
BOTO_CONFIG_SOURCE="$1"

# This is the destination Google Storage location
INPUT_GOOGLE_STORAGE="$2"

# This is the flag to ignore if source uri is missing or throw an error.
IGNORE_IF_SRC_MISSING="${3:-false}"

# Download and extract the tarball.
# We use --no-check-certificate because Google are naughty with their certificates.
wget --no-verbose --no-check-certificate ${GSUTIL_URL}
tar -xzf ${TARBALL}

# Download the boto configuration
aws s3 cp ${BOTO_CONFIG_SOURCE} ${BOTO_CONFIG}

COMMAND="./gsutil/gsutil -m cp -r ${INPUT_GOOGLE_STORAGE} ${OUTPUT1_STAGING_DIR}/"

if [[ ${IGNORE_IF_SRC_MISSING} == "true" ]]; then
  if ! ${COMMAND} ; then
    echo "INFO: no data available to download"
  fi
else
  ${COMMAND}
fi
