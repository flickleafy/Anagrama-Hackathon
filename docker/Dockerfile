ARG JAVA_BASE=adoptopenjdk/openjdk8@sha256:6d3b60d0bda3fb686e50f27e0c0e77e87449b12b70f0a9f666f3ab2b8ab97dc9

FROM ${JAVA_BASE} AS projeto

ARG ANT_VERSION=1.10.7
ARG ANT_SHA512=838ce70c7dbd2b53068ce17b169c0b3fbed5e0ab7be5c707f052418fb6a4a1620f2d4017ceca1379cd25edce3e46d70bb2b5de4e1c5c52e2e1275deec1228084

LABEL org.opencontainers.image.title="Anagrama-Hackathon" \
      org.opencontainers.image.description="Ambiente Java 8 para o projeto Anagrama" \
      br.com.anagrama.userspace="Ubuntu 18.04.3" \
      br.com.anagrama.java="OpenJDK 8u232-b09" \
      br.com.anagrama.ant="1.10.7" \
      br.com.anagrama.jcuda="10.1.0"

ENV ANT_HOME=/opt/apache-ant-1.10.7 \
    ANT_OPTS=-Dplatforms.JDK_8.home=/opt/java/openjdk \
    LANG=C.UTF-8 \
    LC_ALL=C.UTF-8 \
    JAVA_TOOL_OPTIONS=-Dfile.encoding=UTF-8 \
    ANAGRAMA_VARIANTE=cpu \
    PATH=/opt/apache-ant-1.10.7/bin:/opt/java/openjdk/bin:/usr/local/sbin:/usr/local/bin:/usr/sbin:/usr/bin:/sbin:/bin

# O arquivo e seu checksum pertencem à mesma distribuição do Apache Ant.
RUN set -eu; \
    curl --fail --location --retry 3 \
        "https://archive.apache.org/dist/ant/binaries/apache-ant-${ANT_VERSION}-bin.tar.gz" \
        --output "/tmp/apache-ant-${ANT_VERSION}-bin.tar.gz"; \
    printf '%s  %s\n' "$ANT_SHA512" "/tmp/apache-ant-${ANT_VERSION}-bin.tar.gz" \
        | sha512sum -c -; \
    tar -xzf "/tmp/apache-ant-${ANT_VERSION}-bin.tar.gz" -C /opt; \
    rm "/tmp/apache-ant-${ANT_VERSION}-bin.tar.gz"; \
    ant -version

RUN groupadd --gid 10001 anagrama \
    && useradd --uid 10001 --gid anagrama --home-dir /opt/anagrama \
        --shell /bin/sh anagrama \
    && mkdir -p /opt/anagrama \
    && chown anagrama:anagrama /opt/anagrama

WORKDIR /opt/anagrama
COPY --chown=10001:10001 . .

RUN chmod 0555 docker/*.sh \
    && sha256sum -c docker/jcuda-10.1.0.sha256

USER anagrama
RUN ./docker/verificar-ambiente.sh \
    && ant clean test-all jar

ENTRYPOINT ["/opt/anagrama/docker/entrada.sh"]
CMD ["java", "-jar", "dist/Anagrama.jar"]

FROM projeto AS cpu

LABEL br.com.anagrama.variante="cpu"

FROM projeto AS cuda

USER root

ARG CUDA_REPOSITORIO=https://developer.download.nvidia.com/compute/cuda/repos/ubuntu1804/x86_64
ARG CUDA_VERSAO_PACOTE=10.1.243-1

ENV ANAGRAMA_VARIANTE=cuda \
    CUDA_HOME=/usr/local/cuda-10.1 \
    CUDA_VERSION=10.1.243 \
    NVIDIA_VISIBLE_DEVICES=all \
    NVIDIA_DRIVER_CAPABILITIES=compute,utility \
    NVIDIA_REQUIRE_CUDA="cuda>=10.1 driver>=418.39" \
    LD_LIBRARY_PATH=/usr/local/cuda-10.1/lib64:/usr/local/nvidia/lib:/usr/local/nvidia/lib64 \
    PATH=/usr/local/cuda-10.1/bin:/opt/apache-ant-1.10.7/bin:/opt/java/openjdk/bin:/usr/local/sbin:/usr/local/bin:/usr/sbin:/usr/bin:/sbin:/bin

RUN set -eu; \
    mkdir -p /tmp/cuda; \
    curl --fail --location --retry 3 \
        "$CUDA_REPOSITORIO/cuda-license-10-1_${CUDA_VERSAO_PACOTE}_amd64.deb" \
        --output "/tmp/cuda/cuda-license.deb"; \
    curl --fail --location --retry 3 \
        "$CUDA_REPOSITORIO/cuda-cudart-10-1_${CUDA_VERSAO_PACOTE}_amd64.deb" \
        --output "/tmp/cuda/cuda-cudart.deb"; \
    curl --fail --location --retry 3 \
        "$CUDA_REPOSITORIO/cuda-driver-dev-10-1_${CUDA_VERSAO_PACOTE}_amd64.deb" \
        --output "/tmp/cuda/cuda-driver-dev.deb"; \
    curl --fail --location --retry 3 \
        "$CUDA_REPOSITORIO/cuda-cudart-dev-10-1_${CUDA_VERSAO_PACOTE}_amd64.deb" \
        --output "/tmp/cuda/cuda-cudart-dev.deb"; \
    curl --fail --location --retry 3 \
        "$CUDA_REPOSITORIO/cuda-misc-headers-10-1_${CUDA_VERSAO_PACOTE}_amd64.deb" \
        --output "/tmp/cuda/cuda-misc-headers.deb"; \
    curl --fail --location --retry 3 \
        "$CUDA_REPOSITORIO/cuda-nvcc-10-1_${CUDA_VERSAO_PACOTE}_amd64.deb" \
        --output "/tmp/cuda/cuda-nvcc.deb"; \
    printf '%s  %s\n' \
        '78b0812552c480341da7c8f03c2a575270a7a5be67414122f3a171209fb7512d' '/tmp/cuda/cuda-license.deb' \
        'fae5f3830feb7bfb5fce6f1c71c4d8c846d055ad98143e9b58639a5781161a56' '/tmp/cuda/cuda-cudart.deb' \
        '9c6f1d200003fe322905fde41bc5001b0dd7acd35a2e2f7075290681908fe5b4' '/tmp/cuda/cuda-driver-dev.deb' \
        'b1c0a8069a351212bee1d1e645c0811d98f597d7c147b8f3789081838ad7e56b' '/tmp/cuda/cuda-cudart-dev.deb' \
        '7e04ff91eaced323b4187b864288da4975c21671be952b5bc325f8bdb7da9b34' '/tmp/cuda/cuda-misc-headers.deb' \
        'c50eb686651003c78df821d195b09d2ac1337e1f59befc183a63eeb2a2452beb' '/tmp/cuda/cuda-nvcc.deb' \
        | sha256sum -c -; \
    printf '%s\n' \
        'deb http://archive.ubuntu.com/ubuntu bionic main universe' \
        > /etc/apt/sources.list; \
    apt-get update; \
    # A imagem Java contém revisões do terceiro ponto de manutenção. Alguns \
    # pacotes-base são alinhados de forma explícita antes de instalar o GCC 7, \
    # mantendo todas as dependências nas versões declaradas pelo projeto. \
    DEBIAN_FRONTEND=noninteractive apt-get install \
        --yes --no-install-recommends --allow-downgrades \
        build-essential=12.4ubuntu1 \
        gcc-7=7.3.0-16ubuntu3 \
        g++-7=7.3.0-16ubuntu3 \
        gcc-8-base=8-20180414-1ubuntu2 \
        libgcc1=1:8-20180414-1ubuntu2 \
        libstdc++6=8-20180414-1ubuntu2 \
        perl-base=5.26.1-6 \
        binutils-common=2.30-15ubuntu1 \
        libbinutils=2.30-15ubuntu1 \
        binutils-x86-64-linux-gnu=2.30-15ubuntu1; \
    dpkg --install \
        /tmp/cuda/cuda-license.deb \
        /tmp/cuda/cuda-cudart.deb \
        /tmp/cuda/cuda-driver-dev.deb \
        /tmp/cuda/cuda-cudart-dev.deb \
        /tmp/cuda/cuda-misc-headers.deb \
        /tmp/cuda/cuda-nvcc.deb; \
    ln -sfn /usr/local/cuda-10.1 /usr/local/cuda; \
    ln -sfn /usr/bin/gcc-7 /usr/local/bin/gcc; \
    ln -sfn /usr/bin/g++-7 /usr/local/bin/g++; \
    nvcc -m64 --ptx --gpu-architecture=compute_75 \
        src/controle/cuda/kernels/CudaCombinadorKernel.cu \
        -o /tmp/CudaCombinadorKernel.ptx; \
    grep -F '.version 6.4' /tmp/CudaCombinadorKernel.ptx; \
    grep -F '.target sm_75' /tmp/CudaCombinadorKernel.ptx; \
    rm -rf /tmp/cuda /var/lib/apt/lists/*; \
    test "$(dpkg-query -W -f='${Version}' cuda-cudart-10-1)" = "$CUDA_VERSAO_PACOTE"; \
    test "$(dpkg-query -W -f='${Version}' gcc-7)" = '7.3.0-16ubuntu3'; \
    test "$(dpkg-query -W -f='${Version}' g++-7)" = '7.3.0-16ubuntu3'; \
    gcc --version | grep -F '7.3.0'; \
    nvcc --version | grep -F 'V10.1.243'

LABEL br.com.anagrama.variante="cuda" \
      br.com.anagrama.cuda="10.1.243" \
      br.com.anagrama.driver-minimo="418.39" \
      br.com.anagrama.ptx="6.4" \
      br.com.anagrama.arquitetura-cuda="sm_75"

USER anagrama
RUN ./docker/verificar-ambiente.sh
