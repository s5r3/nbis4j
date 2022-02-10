cmake_minimum_required(VERSION 3.10)

# Project
project(nbis4j)
set(CMAKE_CXX_STANDARD 11)
set(CMAKE_POSITION_INDEPENDENT_CODE on)

# Default values
if ("Linux" STREQUAL ${CMAKE_SYSTEM_NAME})
    set(NBIS_ROOT "/opt/nbis")
elseif ("Windows" STREQUAL ${CMAKE_SYSTEM_NAME})
    set(NBIS_ROOT "C:\\nbis")
endif ()

# ---- CUSTOMIZATION SECTION -------------------------------------------------------------------------------------------

# ----------------------------------------------------------------------------------------------------------------------

if (NOT EXISTS ${NBIS_ROOT})
    message(FATAL_ERROR
            "NBIS_ROOT must be valid. Default location is /opt/nbis (Linux) or C:\\nbis (Windows), if you use  "
            "non-default location for NBIS, please set appropriate location in CUSTOMIZATION SECTION as below: \n"
            "set(NBIS_ROOT \"/path/to/nbis\")")
endif ()

# Direcotries
include_directories(${NBIS_ROOT}/include)
file(GLOB NFIQ_SOURCES ${CMAKE_SOURCE_DIR}/src/nbis4j_nfiq.c)
file(GLOB WSQC_SOURCES ${CMAKE_SOURCE_DIR}/src/nbis4j_wsqc.c)

if ("Windows" STREQUAL ${CMAKE_SYSTEM_NAME})
    add_compile_definitions(__MSYS__)
endif ()

if (NOT CMAKE_BUILD_TYPE)
    set(CMAKE_BUILD_TYPE RelWithDebInfo)
endif ()

add_library(nfiq SHARED ${NFIQ_SOURCES})
add_library(wsqc SHARED ${WSQC_SOURCES})
target_link_libraries(nfiq
        ${NBIS_ROOT}/lib/libwsq.a
        ${NBIS_ROOT}/lib/libfet.a
        ${NBIS_ROOT}/lib/libioutil.a
        ${NBIS_ROOT}/lib/libjpegl.a
        ${NBIS_ROOT}/lib/libnfiq.a
        ${NBIS_ROOT}/lib/libmlp.a
        ${NBIS_ROOT}/lib/libcblas.a
        ${NBIS_ROOT}/lib/libutil.a
        ${NBIS_ROOT}/lib/libmindtct.a
        -lm)
target_link_libraries(wsqc
        ${NBIS_ROOT}/lib/libwsq.a
        ${NBIS_ROOT}/lib/libfet.a
        ${NBIS_ROOT}/lib/libioutil.a
        ${NBIS_ROOT}/lib/libjpegl.a
        ${NBIS_ROOT}/lib/libutil.a
        -lm)

# To be invoked be Gradle
install(TARGETS nfiq DESTINATION native)
install(TARGETS wsqc DESTINATION native)
