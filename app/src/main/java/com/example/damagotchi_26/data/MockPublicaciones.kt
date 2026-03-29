package com.example.damagotchi_26.data

val publicacionesMock = listOf(

    PublicacionInformativa(
        id = "1",
        titulo = "Hidratación en el embarazo",
        contenido = "Mantener una buena hidratación es importante durante el embarazo. Puede ayudar al bienestar general y al equilibrio del organismo.",
        categoria = CategoriaInformativa.HIDRATACION,
        semanaInicio = 1,
        semanaFin = 40,
        rolDestino = listOf(RolUsuario.MAMA, RolUsuario.ACOMPANANTE),
        destacada = true,
        fuente = "ACOG",
        fuenteURL = "https://www.acog.org/"
    ),

    PublicacionInformativa(
        id = "2",
        titulo = "Mascotas y embarazo",
        contenido = "Convivir con animales es seguro en la mayoría de los casos, pero es importante tener precaución con la toxoplasmosis, especialmente al manipular areneros.",
        categoria = CategoriaInformativa.MASCOTAS,
        semanaInicio = 1,
        semanaFin = 40,
        rolDestino = listOf(RolUsuario.MAMA),
        destacada = false,
        fuente = "CDC",
        fuenteURL = "https://www.cdc.gov/"
    ),

    PublicacionInformativa(
        id = "3",
        titulo = "Alimentación vegetariana o vegana",
        contenido = "Una dieta vegetariana o vegana puede ser compatible con el embarazo, pero es importante prestar atención a nutrientes como hierro, vitamina B12 y proteínas.",
        categoria = CategoriaInformativa.ALIMENTACION,
        semanaInicio = 1,
        semanaFin = 40,
        rolDestino = listOf(RolUsuario.MAMA),
        destacada = true,
        fuente = "NHS",
        fuenteURL = "https://www.nhs.uk/"
    ),

    PublicacionInformativa(
        id = "4",
        titulo = "Seguimiento en el tercer trimestre",
        contenido = "En el tercer trimestre es habitual aumentar el seguimiento y preparar el parto. Es importante atender a cambios y mantener contacto con profesionales.",
        categoria = CategoriaInformativa.SALUD,
        semanaInicio = 28,
        semanaFin = 40,
        rolDestino = listOf(RolUsuario.MAMA, RolUsuario.ACOMPANANTE),
        destacada = true,
        fuente = "OMS",
        fuenteURL = "https://www.who.int/"
    ),

    PublicacionInformativa(
        id = "5",
        titulo = "VIH y embarazo",
        contenido = "El seguimiento médico adecuado durante el embarazo reduce significativamente el riesgo de transmisión al bebé.",
        categoria = CategoriaInformativa.SALUD,
        semanaInicio = 1,
        semanaFin = 40,
        rolDestino = listOf(RolUsuario.MAMA),
        destacada = false,
        fuente = "NIH",
        fuenteURL = "https://hivinfo.nih.gov/"
    )
)