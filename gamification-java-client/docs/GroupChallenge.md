
# GroupChallenge

## Properties
Name | Type | Description | Notes
------------ | ------------- | ------------- | -------------
**attendees** | [**List&lt;Attendee&gt;**](Attendee.md) |  |  [optional]
**challengeModel** | **String** |  |  [optional]
**challengePointConcept** | [**PointConceptRef**](PointConceptRef.md) |  |  [optional]
**challengeTarget** | **Double** |  |  [optional]
**end** | [**OffsetDateTime**](OffsetDateTime.md) |  |  [optional]
**gameId** | **String** |  |  [optional]
**id** | **String** |  |  [optional]
**instanceName** | **String** |  |  [optional]
**origin** | **String** |  |  [optional]
**priority** | **Integer** |  |  [optional]
**reward** | [**Reward**](Reward.md) |  |  [optional]
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



