#!/bin/sh

set -eu

falhar()
{
    printf 'Erro de compatibilidade: %s\n' "$1" >&2
    exit 1
}

avisar()
{
    printf 'Aviso de compatibilidade: %s\n' "$1" >&2
}

kernel_esta_no_intervalo_suportado()
{
    versao_sem_sufixo=${1%%-*}
    versao_maior=${versao_sem_sufixo%%.*}
    versao_restante=${versao_sem_sufixo#*.}
    versao_menor=${versao_restante%%.*}

    case "$versao_maior:$versao_menor" in
        *[!0-9:]*|'') return 1 ;;
    esac

    if [ "$versao_maior" -eq 4 ] && [ "$versao_menor" -ge 15 ]
    then
        return 0
    fi
    if [ "$versao_maior" -eq 5 ] && [ "$versao_menor" -le 4 ]
    then
        return 0
    fi
    return 1
}

[ -r /etc/os-release ] || falhar '/etc/os-release não está disponível'
. /etc/os-release
[ "${VERSION_ID:-}" = '18.04' ] \
    || falhar "userspace esperado Ubuntu 18.04; encontrado ${VERSION_ID:-desconhecido}"

java_saida=$(java -version 2>&1)
printf '%s\n' "$java_saida" | grep -F '1.8.0_232' >/dev/null \
    || falhar 'o runtime Java não é OpenJDK 8u232'

javac_saida=$(javac -version 2>&1)
printf '%s\n' "$javac_saida" | grep -F '1.8.0_232' >/dev/null \
    || falhar 'o compilador Java não é OpenJDK 8u232'

ant_saida=$(ant -version 2>&1)
printf '%s\n' "$ant_saida" | grep -F '1.10.7' >/dev/null \
    || falhar 'o Apache Ant não está na versão 1.10.7'

sha256sum -c docker/jcuda-10.1.0.sha256 >/dev/null \
    || falhar 'um ou mais artefatos JCuda 10.1.0 foram alterados'

variante=${ANAGRAMA_VARIANTE:-cpu}
case "$variante" in
    cpu)
        ;;
    cuda)
        command -v nvcc >/dev/null 2>&1 \
            || falhar 'nvcc não está disponível na imagem CUDA'
        command -v gcc >/dev/null 2>&1 \
            || falhar 'GCC não está disponível na imagem CUDA'
        command -v g++ >/dev/null 2>&1 \
            || falhar 'G++ não está disponível na imagem CUDA'
        nvcc_saida=$(nvcc --version 2>&1)
        printf '%s\n' "$nvcc_saida" | grep -F 'V10.1.243' >/dev/null \
            || falhar 'nvcc não pertence ao CUDA 10.1.243'
        [ "$(dpkg-query -W -f='${Version}' cuda-cudart-10-1 2>/dev/null)" = '10.1.243-1' ] \
            || falhar 'o CUDA Runtime não está na versão 10.1.243-1'
        [ "$(dpkg-query -W -f='${Version}' cuda-cudart-dev-10-1 2>/dev/null)" = '10.1.243-1' ] \
            || falhar 'os cabeçalhos CUDA Runtime não estão na versão 10.1.243-1'
        [ "$(dpkg-query -W -f='${Version}' gcc-7 2>/dev/null)" = '7.3.0-16ubuntu3' ] \
            || falhar 'o compilador host CUDA não é o GCC 7.3 do Ubuntu base'
        [ "$(dpkg-query -W -f='${Version}' g++-7 2>/dev/null)" = '7.3.0-16ubuntu3' ] \
            || falhar 'o compilador C++ host CUDA não é o G++ 7.3 do Ubuntu base'
        ;;
    *)
        falhar "variante desconhecida: $variante"
        ;;
esac

# O kernel exibido por uname pertence ao host; uma imagem Docker não contém
# nem inicializa seu próprio kernel Linux.
kernel_host=$(uname -r)
if ! kernel_esta_no_intervalo_suportado "$kernel_host"
then
    if [ "${ANAGRAMA_EXIGIR_AMBIENTE_ALVO:-0}" = '1' ]
    then
        falhar "kernel do host $kernel_host não pertence ao intervalo suportado de 4.15 a 5.4"
    fi
    avisar "kernel do host $kernel_host não pertence ao intervalo suportado de 4.15 a 5.4; Docker não consegue substituí-lo"
fi

printf 'Ambiente validado: Ubuntu %s, Java 8u232, Ant 1.10.7, variante %s, kernel do host %s\n' \
    "$VERSION_ID" "$variante" "$kernel_host"
