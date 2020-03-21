cmake_minimum_required(VERSION 3.15)
project(Lab_8)

set(CMAKE_CXX_STANDARD 17)

set(MAIN_SRCS "main.cpp")
add_executable(main ${MAIN_SRCS})


add_library(MPI_LIB SHARED IMPORTED "C:/Program Files (x86)/Microsoft SDKs/MPI/Lib/x64/msmpi.lib" "C:/Program Files (x86)/Microsoft SDKs/MPI/Lib/x64/msmpifec.lib" "C:/Program Files (x86)/Microsoft SDKs/MPI/Lib/x64/msmpifmc.lib") # or STATIC instead of SHARED

include_directories("C:/Program Files (x86)/Microsoft SDKs/MPI/Include")

target_link_libraries(main MPI_LIB) # also adds the required include path
