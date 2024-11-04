#!/bin/bash

if ! command -v dot &> /dev/null
then
    echo "The 'dot' command is not found. This is required to generate SVGs from the Graphviz files."
    echo "Installation instructions:"
    echo "  - On macOS: You can install Graphviz using Homebrew with the command: 'brew install graphviz'"
    echo "  - On Ubuntu: You can install Graphviz using APT with the command: 'sudo apt-get install graphviz'"
    exit 1
fi

if ! command -v svgo &> /dev/null
then
    echo "The 'svgo' command is not found. This is required to cleanup and compress SVGs."
    echo "Installation instructions available at https://github.com/svg/svgo."
    exit 1
fi

if grep -P "" /dev/null > /dev/null 2>&1; then
  GREP_COMMAND=grep
elif command -v ggrep &> /dev/null; then
  GREP_COMMAND=ggrep
else
  echo "You don't have a version of 'grep' installed which supports Perl regular expressions."
  echo "On MacOS you can install one using Homebrew with the command: 'brew install grep'"
  exit 1
fi

module_paths=$(${GREP_COMMAND} -oP 'include\("\K[^"]+' settings.gradle.kts)

echo "$module_paths" | while read -r module_path;
do
  echo "run $module_path"

  file_name="dep_graph${module_path//:/_}"
  file_name="${file_name//-/_}"
  echo "file $file_name"

  path="${module_path:1}"
  path=${path//:/\/}
  readme_path="${path}/README.md"
  echo "readme $readme_path"

  relative_image_path="../"
  depth=$(awk -F: '{print NF-1}' <<< "${module_path}")
  for ((i=1; i<$depth; i++)); do
      relative_image_path+="../"
  done
  relative_image_path+="docs/images/graphs/${file_name}.svg"
  echo "image path $relative_image_path"

  echo "# ${module_path} module" > "$readme_path"
  echo "## Dependency graph" >> "$readme_path"
  echo "![Dependency graph](${relative_image_path})" >> "$readme_path"

  ./gradlew "$module_path:generateModulesGraphvizText" -Pmodules.graph.output.gv="build/${file_name}.gv" --no-build-cache --no-configuration-cache

  dot -Tsvg "build/${file_name}.gv" |
    svgo --multipass --pretty --output="docs/images/graphs/${file_name}.svg" -

#  rm "${file_name}.gv"
done