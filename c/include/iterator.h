/*******************************************************************************
 * File         : iterator.h
 * Author(s)    : Tekin Ozbek <tekin@tekinozbek.com>
 ******************************************************************************/

#ifndef ODS_ITERATOR_H_
#define ODS_ITERATOR_H_

typedef struct iterator_t {

    /* FUNCTION
     *      next
     *
     * DESCRIPTION
     *      Advances to the next element if possible, or returns 0 if there are
     *      no more elements left. In case of reverse iteration, this function
     *      will advance to the previous element.
     *
     * RETURN VALUE
     *      Returns 1 if there is a next element, 0 otherwise.
     */
    int (*next)(struct iterator_t*);

    /* FUNCTION
     *      elem
     *
     * RETURN VALUE
     *      Returns pointer to the element at current position. This is a
     *      pointer to the data within the data structure, not a copy.
     */
    void* (*elem)(struct iterator_t*);

    /* FUNCTION
     *      elem
     *
     * DESCRIPTION
     *      Cleans up the memory used by the iterator.
     */
    void (*dispose)(struct iterator_t*);

    void* istruct;

} iterator_t;

#endif
