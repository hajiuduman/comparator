comparator
==========

Java Comparator Utility classes

SortUtil allows a user to sort a collection (or array, iterator, etc) on any
arbitrary set of properties exposed by the objects contained within the
collection.

<p>
The sort tool can handle all of the collection types supported by #foreach
and the same constraints apply as well as the following. Every object in the
collection must support the set of properties selected to sort on. Each
property which is to be sorted on must return one of the follow:
<ul>
<li>Primitive type: e.g. int, char, long etc</li>
<li>Standard Object: e.g. String, Integer, Long etc</li>
<li>Object which implements the Comparable interface.</li>
</ul>
</p>

<p>
During the sort operation all properties are compared by calling compareTo()
with the exception of Strings for which compareToIgnoreCase() is called.
</p>

<p>
The sort is performed by calling Collections.sort() after marshaling the
collection to sort into an appropriate collection type. The original
collection will not be re-ordered; a new list containing the sorted elements
will always be returned.
