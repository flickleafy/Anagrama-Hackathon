#!/bin/sh

set -eu

falhar()
{
    printf 'Erro no host Docker: %s\n' "$1" >&2
    exit 1
}

avisar()
{
    printf 'Aviso no host Docker: %s\n' "$1" >&2
}

modo=${1:-cpu}
case "$modo" in
    cpu|cuda) ;;
    *) falhar "modo desconhecido: $modo" ;;
esac

command -v docker >/dev/null 2>&1 || falhar 'o comando docker não foi encontrado'

versao_docker=$(docker version --format '{{.Server.Version}}' 2>/dev/null) \
    || falhar 'não foi possível acessar o daemon Docker'
arquitetura=$(docker info --format '{{.Architecture}}' 2>/dev/null) \
    || falhar 'não foi possível consultar a arquitetura do daemon'
kernel_host=$(docker info --format '{{.KernelVersion}}' 2>/dev/null) \
    || falhar 'não foi possível consultar o kernel do host'

case "$arquitetura" in
    amd64|x86_64) ;;
    *) falhar "a imagem JCuda é exclusiva para linux/amd64; daemon encontrado: $arquitetura" ;;
esac

docker_maior=${versao_docker%%.*}
docker_restante=${versao_docker#*.}
docker_menor=${docker_restante%%.*}
if [ "$docker_maior" -lt 18 ] \
    || { [ "$docker_maior" -eq 18 ] && [ "$docker_menor" -lt 6 ]; }
then
    falhar "Docker $versao_docker é anterior ao mínimo 18.06"
fi

docker_suportado=0
if [ "$docker_maior" -eq 18 ] && [ "$docker_menor" -ge 6 ]
then
    docker_suportado=1
elif [ "$docker_maior" -eq 19 ] && [ "$docker_menor" -le 3 ]
then
    docker_suportado=1
fi

kernel_sem_sufixo=${kernel_host%%-*}
kernel_maior=${kernel_sem_sufixo%%.*}
kernel_restante=${kernel_sem_sufixo#*.}
kernel_menor=${kernel_restante%%.*}
kernel_suportado=0
if [ "$kernel_maior" -eq 4 ] && [ "$kernel_menor" -ge 15 ]
then
    kernel_suportado=1
elif [ "$kernel_maior" -eq 5 ] && [ "$kernel_menor" -le 4 ]
then
    kernel_suportado=1
fi

if [ "$docker_suportado" -ne 1 ]
then
    if [ "${ANAGRAMA_EXIGIR_AMBIENTE_ALVO:-0}" = '1' ]
    then
        falhar "Docker $versao_docker não pertence ao intervalo suportado de 18.06 a 19.03"
    fi
    avisar "Docker $versao_docker não pertence ao intervalo suportado de 18.06 a 19.03"
fi

if [ "$kernel_suportado" -ne 1 ]
then
    if [ "${ANAGRAMA_EXIGIR_AMBIENTE_ALVO:-0}" = '1' ]
    then
        falhar "kernel $kernel_host não pertence ao intervalo suportado de 4.15 a 5.4"
    fi
    avisar "kernel $kernel_host é compartilhado pelos contêineres e não pertence ao intervalo suportado de 4.15 a 5.4"
fi

if [ "$modo" = 'cuda' ]
then
    runtimes=$(docker info --format '{{json .Runtimes}}' 2>/dev/null)
    printf '%s\n' "$runtimes" | grep -F '"nvidia"' >/dev/null \
        || falhar 'o runtime nvidia não está registrado no daemon Docker'
fi

printf 'Host validado: Docker %s, linux/%s, kernel %s, modo %s\n' \
    "$versao_docker" "$arquitetura" "$kernel_host" "$modo"
