Project(Intech)
cmake_minimum_required(VERSION 2.8)



include(options.cmake)

set (
	EXECUTABLE_OUTPUT_PATH
	${CMAKE_CURRENT_LIST_DIR}/bin/
	)

set(LIBRARY_DIR ${CMAKE_CURRENT_SOURCE_DIR}/library)
include_directories(${LIBRARY_DIR})

include_directories(${CMAKE_CURRENT_SOURCE_DIR}/pc/include)

set(hexapode_SRC)
AUX_SOURCE_DIRECTORY(${CMAKE_CURRENT_SOURCE_DIR}/pc/ hexapode_SRC)

add_executable(hexapode.out ${hexapode_SRC})
target_link_libraries(hexapode.out c m c)
