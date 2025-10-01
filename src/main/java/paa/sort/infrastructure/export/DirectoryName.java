package paa.sort.infrastructure.export;

import paa.sort.domain.testdata.DataType;

/**
 * Enum que representa os nomes de diretorios usados para exportacao de arrays
 */
public enum DirectoryName {
    ALEATORIO("aleatorio"),
    ORDENADO("ordenado"),
    ORDENADO_INVERSO("ordenado_inverso"),
    MUITOS_DUPLICADOS("muitos_duplicados"),
    PIOR_CASO("pior_caso");

    private final String directoryName;

    DirectoryName(String directoryName) {
        this.directoryName = directoryName;
    }

    public String getDirectoryName() {
        return directoryName;
    }

    /**
     * Converte DataType para DirectoryName correspondente
     */
    public static DirectoryName fromDataType(DataType dataType) {
        return switch (dataType) {
            case RANDOM -> ALEATORIO;
            case SORTED -> ORDENADO;
            case REVERSE_SORTED -> ORDENADO_INVERSO;
            case MANY_DUPLICATES -> MUITOS_DUPLICADOS;
            case WORST_CASE -> PIOR_CASO;
        };
    }
}
