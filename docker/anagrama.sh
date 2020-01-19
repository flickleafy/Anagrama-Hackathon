#!/bin/sh

set -eu

mostrar_uso()
{
    printf '%s\n' \
        'Uso: ./docker/anagrama.sh <cpu|cuda> <build|run|test|verify>' \
        '' \
        '  build   constrói a imagem selecionada' \
        '  run     executa a aplicação interativa' \
        '  test    executa test-all em CPU ou o teste CUDA nativo' \
        '  verify  mostra e valida as versões do contêiner'
}

variante=${1:-}
acao=${2:-}
case "$variante" in
    cpu)
        arquivo_compose=docker-compose.yml
        servico=anagrama-cpu
        ;;
    cuda)
        arquivo_compose=docker-compose.cuda.yml
        servico=anagrama-cuda
        ;;
    *)
        mostrar_uso >&2
        exit 2
        ;;
esac

case "$acao" in
    build|run|test|verify) ;;
    *)
        mostrar_uso >&2
        exit 2
        ;;
esac

raiz_projeto=$(CDPATH= cd -- "$(dirname -- "$0")/.." && pwd)
cd "$raiz_projeto"

if docker compose version >/dev/null 2>&1
then
    compose_versao=$(docker compose version --short 2>/dev/null)
    compose()
    {
        docker compose "$@"
    }
elif command -v docker-compose >/dev/null 2>&1
then
    compose_versao=$(docker-compose version --short 2>/dev/null)
    compose()
    {
        docker-compose "$@"
    }
else
    printf 'Erro: Docker Compose não foi encontrado.\n' >&2
    exit 1
fi

# O formato 2.4 requer Docker Compose 1.21
compose_sem_prefixo=${compose_versao#v}
compose_maior=${compose_sem_prefixo%%.*}
compose_restante=${compose_sem_prefixo#*.}
compose_menor=${compose_restante%%.*}
compose_suportado=0
case "$compose_maior:$compose_menor" in
    *[!0-9:]*|'') ;;
    *)
        if [ "$compose_maior" -eq 1 ] \
            && [ "$compose_menor" -ge 21 ] \
            && [ "$compose_menor" -le 25 ]
        then
            compose_suportado=1
        fi
        ;;
esac

if [ "$compose_suportado" -ne 1 ]
then
    if [ "${ANAGRAMA_EXIGIR_AMBIENTE_ALVO:-0}" = '1' ]
    then
        printf 'Erro: Docker Compose %s não pertence ao intervalo suportado de 1.21 a 1.25.\n' \
            "$compose_versao" >&2
        exit 1
    fi
    printf 'Aviso: Docker Compose %s não pertence ao intervalo suportado de 1.21 a 1.25.\n' \
        "$compose_versao" >&2
fi

# A construção da imagem CUDA não precisa de uma GPU; as demais ações
# validam também a presença do runtime NVIDIA quando aplicável.
if [ "$acao" = 'build' ]
then
    ./docker/verificar-host.sh cpu
else
    ./docker/verificar-host.sh "$variante"
fi

case "$acao" in
    build)
        compose -f "$arquivo_compose" build "$servico"
        ;;
    run)
        compose -f "$arquivo_compose" run --rm "$servico"
        ;;
    test)
        if [ "$variante" = 'cpu' ]
        then
            compose -f "$arquivo_compose" run --rm "$servico" ant test-all
        else
            compose -f "$arquivo_compose" run --rm "$servico" \
                ant test-all test-performance-cuda
        fi
        ;;
    verify)
        # O entrypoint valida o ambiente antes de executar este comando.
        compose -f "$arquivo_compose" run --rm "$servico" true
        ;;
esac
