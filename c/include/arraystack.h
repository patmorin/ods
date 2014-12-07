/*******************************************************************************
 * File         : arraystack.h
 * Author(s)    : Tekin Ozbek <tekin@tekinozbek.com>
 *
 * Implementation of an array-based stack. The struct holds ownership of the
 * backing array and must be initialized at the beginning and disposed at the
 * end.
 *
 * The stack will make copies of inserted elements. To avoid data duplication,
 * you may store pointers in the stack. However, the stack will not claim
 * ownership of these pointers; they must be freed by the user.
 ******************************************************************************/

#ifndef ODS_ARRAYSTACK_H_
#define ODS_ARRAYSTACK_H_

typedef struct {

    size_t alloc_length;  /* allocated length of the array, in bytes */
    size_t length;        /* logical length of the stack, in num elems */
    size_t elem_size;     /* size of elements that are stored */
    void* array;          /* backing array of the stack */
    
} arraystack_t;

/* FUNCTION
 *      ods_arraystack_init
 *
 * DESCRIPTION
 *      Initializes an arraystack_t struct with an array size of 1.
 *      This function must be called before any other arraystack-
 *      related functions. After a call to this function, the stack will be
 *      considered INITIALIZED.
 *
 * PARAMETERS
 *      s           A valid (allocated) pointer to an arraystack_t struct.
 *      elem_size   Size of the elements that will be stored in the stack.
 */
extern void ods_arraystack_init(arraystack_t* s,
                                size_t elem_size);

/* FUNCTION
 *      ods_arraystack_resize
 *
 * DESCRIPTION
 *      Resizes the backing array. The resize length is min(n * 2, 1).
 *
 * PARAMETERS
 *      s           A valid (allocated) pointer to an initialized arraystack_t
 *                  struct.
 */
static void ods_arraystack_resize(arraystack_t* s);

/* FUNCTION
 *      ods_arraystack_add
 *
 * DESCRIPTION
 *      Adds an element to the specified position in the stack.
 *
 * PARAMETERS
 *      s           A valid (allocated) pointer to an initialized arraystack_t
 *                  struct.
 *      pos         The position where the element will be inserted.
 *      elem        Pointer to the element that will inserted to the stack. This
 *                  argument cannot be a null pointer. The memory pointed to by
 *                  this argument will be copied into the array. To avoid data
 *                  duplication, store pointers instead of elements and pass
 *                  double pointers to this argument.
 */
extern void ods_arraystack_add(arraystack_t* s,
                               size_t pos,
                               void* elem);

/* FUNCTION
 *      ods_arraystack_get
 *
 * DESCRIPTION
 *      Gets the element at the specified position.
 *
 * PARAMETERS
 *      s           A valid (allocated) pointer to an initialized arraystack_t
 *                  struct.
 *      pos         The position of the element that will be retrieved.
 *      elem        Pointer to a memory with at least elem_size bytes allocated.
 *                  The data at pos will be copied into this memory.
 */
extern void ods_arraystack_get(arraystack_t* s,
                               size_t pos,
                               void* elem_out);

/* FUNCTION
 *      ods_arraystack_set
 *
 * DESCRIPTION
 *      Sets the element at the specified position. The old element can be
 *      returned through an output parameter.
 *
 * PARAMETERS
 *      s           A valid (allocated) pointer to an initialized arraystack_t
 *                  struct.
 *      pos         The position of the element that will be set.
 *      elem        Pointer to the new element that will be copied into the
 *                  stack.
 *      old_elem    If not a null pointer, the old element at pos will be copied
 *                  into the memory pointed to by this pointer.
 */
extern void ods_arraystack_set(arraystack_t* s,
                               size_t pos,
                               void* elem,
                               void* old_elem);

/* FUNCTION
 *      ods_arraystack_remove
 *
 * DESCRIPTION
 *      Removes the element at the specified position. The removed element can
 *      be returned through an output parameter.
 *
 * PARAMETERS
 *      s           A valid (allocated) pointer to an initialized arraystack_t
 *                  struct.
 *      pos         The position of the element to be removed.
 *      elem_out    If not a null pointer, the removed element will be coped
 *                  into the memory pointed to by this pointer.
 */
extern void ods_arraystack_remove(arraystack_t* s,
                                  size_t pos,
                                  void* elem_out);

/* FUNCTION
 *      ods_arraystack_clear
 *
 * DESCRIPTION
 *      Clears the stack.
 *
 * PARAMETERS
 *      s           A valid (allocated) pointer to an initialized arraystack_t
 *                  struct.
 */
extern void ods_arraystack_clear(arraystack_t* s);

/* FUNCTION
 *      ods_arraystack_copy
 *
 * DESCRIPTION
 *      Copies elements from a source array to the specified position on the
 *      stack.
 *
 * PARAMETERS
 *      s           A valid (allocated) pointer to an initialized arraystack_t
 *                  struct.
 *      pos         The position where the elements from the source array will
 *                  be inserted.
 *      src         Pointer to a block of memory with at least num_elems *
 *                  elem_size bytes allocated.
 *      num_elems   The number of elements that will be copied from src into the
 *                  stack.
 */
extern void ods_arraystack_copy(arraystack_t* s,
                                size_t pos,
                                void* src,
                                size_t num_elems);

/* FUNCTION
 *      ods_arraystack_dispose
 *
 * DESCRIPTION
 *      Releases allocated memory. Make sure to call this function to avoid
 *      memory leaks. The stack, after a call to this function, is considered to
 *      be UNINITIALIZED and must be re-initialized to be able to use again.
 *
 * PARAMETERS
 *      s           A valid (allocated) pointer to an initialized arraystack_t
 *                  struct.
 */
extern void ods_arraystack_dispose(arraystack_t* s);

#endif
 
