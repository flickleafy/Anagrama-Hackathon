/*
 * Copyright (C) 2019 Enzo Erbano
 *
 * Attribution-NonCommercial-ShareAlike 4.0 International (CC BY-NC-SA 4.0)
 */
extern "C"
__global__ void combinar_indices(
    unsigned long long inicio,
    unsigned int quantidade,
    int dimensoes,
    const int *tamanhos,
    int *saida)
{
    // Cada thread materializa uma posição linear do lote atual.
    unsigned int local = blockIdx.x * blockDim.x + threadIdx.x;
    if (local >= quantidade)
    {
        return;
    }

    unsigned long long valor = inicio + local;
    // A decomposição em radix misto produz um índice para cada coleção
    // do plano, na mesma ordem usada pela contingência em Java.
    for (int dimensao = dimensoes - 1; dimensao >= 0; dimensao--)
    {
        int tamanho = tamanhos[dimensao];
        saida[local * dimensoes + dimensao] = (int)(valor % tamanho);
        valor /= tamanho;
    }
}
