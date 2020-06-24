# kafka-avro

### Generic Records
We can use avro classes to read schema and avro type data

### Specific Records
We can use avsc schema and maven plugin to generate classes using which we can write and read data in avro format.

### Reflection Records
We can generate avsc schema using POJO classes. Using this generated schema we can read and write data in avro format.

### Schema Evolution
There are 4 types of evolution.
1) Backward -> We can read data using new schema. We should use default value for a field.
2) Forward -> We can read new data using older schema. We should use default value for a field.
3) Full -> Both forward and backward are compatible. We should use default value for a field.
4) Breaking -> It will break and we can't read schema
In short for proper schema evolution, we should consider using default values for the fields.
