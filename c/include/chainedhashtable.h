/*******************************************************************************
 * File         : chainedhashtable.h
 * Author(s)    : Tekin Ozbek <tekin@tekinozbek.com>
 ******************************************************************************/

#ifndef ODS_CHAINEDHASHTABLE_H_
#define ODS_CHAINEDHASHTABLE_H_

#include <stdlib.h>

#include <dllist.h>

typedef struct chainedhashtable_t {
    
    dllist_t* array;
    
    size_t  length;
    size_t  elem_size;
    size_t  w;
    size_t  d;
    int     z;
    
    int (*hashcode)(void *);
    
} chainedhashtable_t;

/* FUNCTION
 *      chainedhashtable_add
 *
 * DESCRIPTION
 *      Adds an element if it's not in the table already.
 *
 * PARAMETERS
 *      table       A valid pointer to an initialized chainedhashtable_t struct.
 *      elem        The element that will be copied into the table.
 * 
 * RETURN VALUES
 *      Returns 1 if added successfully, 0 otherwise (it already exists in the
 *      table).
 */
extern int chainedhashtable_add(chainedhashtable_t* table,
                                void* elem);

/* FUNCTION
 *      chainedhashtable_clear
 *
 * DESCRIPTION
 *      Clears the table, reduces the size of the table array.
 *
 * PARAMETERS
 *      table       A valid pointer to an initialized chainedhashtable_t struct.
 */
extern void chainedhashtable_clear(chainedhashtable_t* table);

/* FUNCTION
 *      chainedhashtable_dispose
 *
 * DESCRIPTION
 *      Cleans up initialized lists and deallocates all memory.
 *
 * PARAMETERS
 *      table       A valid pointer to an initialized chainedhashtable_t struct.
 */
extern void chainedhashtable_dispose(chainedhashtable_t* table);

/* FUNCTION
 *      chainedhashtable_find
 *
 * DESCRIPTION
 *      Searches for an element in the hash table.
 *
 * PARAMETERS
 *      table       A valid pointer to an initialized chainedhashtable_t struct.
 *      elem        The element that will be searched for in the table.
 * 
 * RETURN VALUES
 *      Returns 1 if the element was found, 0 otherwise.
 */
extern int chainedhashtable_find(chainedhashtable_t* table,
                                 void* elem);

/* FUNCTION
 *      chainedhashtable_init
 *
 * DESCRIPTION
 *      Initializes a chainedhashtable_t struct.
 *
 * PARAMETERS
 *      table       A valid pointer to a chainedhashtable_t struct.
 *      elem_size   Size of the elements that will be stored in the table.
 *      hashcode    Pointer to a hash function. This function must take a void *
 *                  as a parameter and return an int. The better the function,
 *                  the more efficient the table will be. Cannot be null.
 *      random      Pointer to a random function that takes no parameters and
 *                  returns an int value. If null, the default rand() from the
 *                  standard library is used.
 */
extern void chainedhashtable_init(chainedhashtable_t *table,
                                  size_t elem_size,
                                  int (*hashcode)(void *),
                                  int (*random)(void));

/* FUNCTION
 *      chainedhashtable_remove
 *
 * DESCRIPTION
 *      Removes an element from the table.
 *
 * PARAMETERS
 *      table       A valid pointer to an initialized chainedhashtable_t struct.
 *      elem        The element that will be searched for in the table.
 */
extern void chainedhashtable_remove(chainedhashtable_t* table,
                                    void* elem);

#endif
