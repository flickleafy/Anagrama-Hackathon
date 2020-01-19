#!/bin/sh

set -eu

# Valida os limites da imagem em toda execução, inclusive quando o comando
# padrão é substituído por um alvo Ant.
/opt/anagrama/docker/verificar-ambiente.sh

exec "$@"
