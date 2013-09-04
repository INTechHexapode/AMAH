cmake_minimum_required(VERSION 2.8)

set (
	EXECUTABLE_OUTPUT_PATH
	${CMAKE_CURRENT_LIST_DIR}/../bin/
	)


set(LIBRARY_DIR ${CMAKE_CURRENT_SOURCE_DIR}/library)
include_directories(${LIBRARY_DIR})
macro(add_projects PROJECTS)
	foreach(i ${PROJECTS})
		include_directories(${CMAKE_CURRENT_SOURCE_DIR}/${i}/include)
		set(${i}_SRC)
		AUX_SOURCE_DIRECTORY(${CMAKE_CURRENT_SOURCE_DIR}/${i}/src ${i}_SRC)
		AUX_SOURCE_DIRECTORY(${CMAKE_CURRENT_SOURCE_DIR}/${i}/ ${i}_SRC)
		set_directory_properties(PROPERTIES ADDITIONAL_MAKE_CLEAN_FILES ${i}.hex)

		add_executable(${i}.out ${${i}_SRC})
		target_link_libraries(${i}.out c m c)
		#set_target_properties(${i}.out PROPERTIES LINKER_LANGUAGE CXX)
		#add_custom_target(${i}.hex ALL DEPENDS ${i}.out)
	endforeach()
endmacro()

add_subdirectory(${CMAKE_CURRENT_SOURCE_DIR}/reseauDeNeurones)
