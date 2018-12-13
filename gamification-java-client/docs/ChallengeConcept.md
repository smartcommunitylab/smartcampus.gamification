
# ChallengeConcept

## Properties
Name | Type | Description | Notes
------------ | ------------- | ------------- | -------------
**completed** | **Boolean** |  |  [optional]
**dateCompleted** | [**OffsetDateTime**](OffsetDateTime.md) |  |  [optional]
**end** | [**OffsetDateTime**](OffsetDateTime.md) |  |  [optional]
**fields** | **Object** |  |  [optional]
**id** | **String** |  |  [optional]
**modelName** | **String** |  |  [optional]
**name** | **String** |  |  [optional]
**origin** | **String** |  |  [optional]
**priority** | **Integer** |  |  [optional]
**start** | [**OffsetDateTime**](OffsetDateTime.md) |  |  [optional]
**state** | [**StateEnum**](#StateEnum) |  |  [optional]
**stateDate** | [**Map&lt;String, OffsetDateTime&gt;**](OffsetDateTime.md) |  |  [optional]


<a name="StateEnum"></a>
## Enum: StateEnum
Name | Value
---- | -----
PROPOSED | &quot;PROPOSED&quot;
ASSIGNED | &quot;ASSIGNED&quot;
ACTIVE | &quot;ACTIVE&quot;
COMPLETED | &quot;COMPLETED&quot;
FAILED | &quot;FAILED&quot;
REFUSED | &quot;REFUSED&quot;
AUTO_DISCARDED | &quot;AUTO_DISCARDED&quot;



