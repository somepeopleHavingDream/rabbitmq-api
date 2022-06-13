# RabbitMQ消息中间件精讲 从0到1驾驭RabbitMQ应用与设计
## 第1章 课程介绍
- 主流消息中间件介绍
    - 主流消息中间件介绍-ActiveMQ
        - ActiveMQ是Apache出品，最流行的，能力强劲的开源消息总线，并且它是一个完全支持JMS规范的消息中间件
        - 其丰富的API、多种集群构建模式使得他成为业界老牌消息中间件，在中小型企业中应用广泛
        - MQ衡量指标：服务性能、数据存储、集群架构
    - 主流消息中间件介绍-KAFKA
        - Kafka是LinkedIn开源的分布式发布-订阅消息系统，目前归属于Apache顶级项目。Kafka主要特点是基于Pull的模式来处理消息消费，追求高吞吐量，一开始的目的就是用于日志收集和传输。0.8版本开始支持复制，不支持事务，对消息的重复、丢失、错误没有严格的要求，适合产生大量数据的互联网服务的数据收集业务
    - 主流消息中间件介绍-RocketMQ
        - RocketMQ是阿里开源的消息中间件，目前也已经孵化为Apache顶级项目，挺是纯Java开发，具有高吞吐量、高可用性、适合大规模分布式系统应用的特点。RocketMQ思路起源于Kafka，它对消息的可靠性及事务做了优化，目前在阿里集团被广泛应用于交易、充值、流计算、消息推送、日志流式处理、binglog分发等场景
    - 主流消息中间件介绍-RabbitMQ
        - RabbitMQ是使用Erlang语言开发的开源消息队列系统，基于AMQP协议来实现。AMQP的主要特征是面向消息、队列、路由（包括点对点和发布/订阅）、可靠性、安全。AMQP协议更多用在企业系统内，对数据一致性、稳定性和可靠性要求很高的场景，对性能和吞吐量的要求还在其次。
## 第2章 低门槛，入门RabbitMQ核心概念
- 初识RabbitMQ
    - RabbitMQ是一个开源的消息代理和队列服务器，用来通过普通协议在完全不同的应用之间共享数据，RabbitMQ是使用Erlang语言来编写的，并且RabbitMQ是基于AMQP协议的
- 哪些大厂在用RabbitMQ，为什么？
    - 滴滴、美团、头条、去哪儿、艺龙……
    - 开源、性能优秀、稳定性保障
    - 提供可靠性消息投递模式（confirm）、返回模式（return）
    - 与SpringAMQP完美的整合、API丰富
    - 集群模式丰富，表达式配置，HA配置，镜像队列模型
    - 保证数据不丢失的前提做到高可靠性、可用性
- RabbitMQ高性能的原因？
    - Erlang语言最初在交换机领域的架构模式，这样使得RabbitMQ在Broker之间进行数据的交互的性能是非常优秀的
    - Erlang的优点：Erlang有着和原生Socket一样的延迟
- 什么是AMQP高级消息队列协议？
    - AMQP全称：Advanced Message Queuing Protocol
    - AMQP翻译：高级消息队列协议
    - AMQP定义：是具有现代特征的二进制协议。是一个提供统一消息服务的应用层标准高级消息队列协议，是应用层协议的一个开放标准，为面向消息的中间件设计
- AMQP协议模型
    - Server
        - Virtual host
            - Exchange(Publisher application)
            - Message Queue(Consumer application)
- AMQP核心概念
    - Server：又称Broker，接受客户端的连接，实现AMQP实体服务
    - Connection：连接，应用程序与Broker的网络连接
    - Channel：网络信道，几乎所有的操作都在Channel中进行，Channel是进行消息读写的通道。客户端可建立多个Channel，每个Channel代表一个会话任务
    - Message: 消息，服务器与应用程序之间传送的数据，由Propertis和Body组成。Properties可以消息进行修饰，比如消息的优先级、延迟等高级特性；Body则就是消息体内容
    - Virtual host：虚拟地址，用于进行逻辑隔离，最上层的消息路由。一个Virtual Host里面可以有若干个Exchange和Queue，同一个Virtual Host里面不能有相同名称的Exchange或Queue
    - Exchange：交换机，接收消息，根据路由键转发消息到绑定的队列
    - Binding：Exchange和Queue之间的虚拟连接，binding中可以包含routing key
    - Routing key：一个路由规则，虚拟机可用它来确定如何路由一个特定消息
    - Queue：也称为Message Queue，消息队列，保存信息并将它们转发给消费者
- 交换机类型
    - Direct Exchange
        - 所有发送到Direct Exchange的消息被转发到RouteKey中指定的Queue（注意：Direct模式可以使用RabbitMQ自带的Exchange：defaultExchange，所以不需要将Exchange进行如何的绑定（binding）操作，消息传递时，RouteKey必须完全匹配才会被队列接收，否则该消息会被抛弃
    - Topic Exchange
        - 所有发送到Topic Exchange的消息被转发到所有关心RouteKey中指定Topic的Queue上
        - Exchange将RouteKey和某Topic进行模糊匹配，此时队列需要绑定一个Topic
    - Fanout Exchange
        - 不处理路由键，只需要简单的将队列绑定到交换机上
        - 发送到交换机的消息都会被转发到与该交换机绑定的所有队列上
        - Fanout交换机转发消息是最快的
## 第3章 渐进式，深入RabbitMQ高级特性
- 消息如何保障100%的投递成功？
    - 什么是生产端的可靠性投递？
        - 保障消息的成功发出
        - 保障MQ节点的成功接收
        - 发送端收到MQ节点（Broker）确认应答
        - 完善的消息进行补偿机制
    - BAT/TMD互联网大厂的解决方案
        - 消息落库，对消息状态进行打标
          -消息的延迟投递，做二次确认，回调检查
- 消费端-幂等性保障
    - 消费端实现幂等性，就意味着，我们的消息永远不会消费多次，即使我们收到了多条一样的消息
    - 业界主流的幂等性操作
        -  唯一Id+指纹码机制，利用数据库主键去重
            - 好处：实现简单
            - 坏处：高并发下有数据库写入的性能瓶颈
            - 解决方案：跟进ID进行分库分表进行算法路由
        - 利用Redis的原子性去实习
            - 使用Redis进行幂等，需要考虑的问题
            - 第一：我们是否要进行数据落库，如果落库的话，关键解决的问题是数据库和缓存如何做到原子性？
            - 第二：如果不进行落库，那么都存储到缓存中，如何设置定时同步的策略？
- Confirm确认消息
    - 消息的确认，是指生产者投递消息后，如果Broker收到消息，则会给我们生产者一个应答
    - 生产者进行接收应答，用来确定这条消息是否正常的发送到Broker，这种方式也是消息的可靠性投递的核心保障
- Return消息机制
    - Return Listener用于处理一些不可路由的消息
    - 我们的消息生产者，通过指定一个Exchange和Routingkey，把消息送达到某一个队列中去，然后我们的消费者监听队列，进行消费处理操作
    - 但是在某些情况下，如果我们在发送消息的时候，当前的exchange不存在或者指定的路由key路由不到，这个时候如果我们需要监听这种不可达的消息，就要使用Return Listener!
- 消费端限流
    - 什么是消费端的限流？
        - 假设一个场景，首先，我们RabbitMQ服务器有上万条未处理的消息，我们随便打开一个消费者客户端，会出现下面情况
            - 巨量的消息瞬间全部推送过来，但是我们单个客户端无法同时处理那么多数据
    - 消费端限流
        - RabbitMQ提供了一种qos（服务质量保证）功能，即在非自动确认消息的前提下，如果一定数目的消息（提供基于consume或者channel设置Qos的值）未被确认前，不进行消费新的消息
- 消费端ACK与重回队列
    - 消费端的手工ACK和NACK
        - 消费端进行消费的时候，如果用于业务异常，我们可以进行日志的记录，然后进行补偿
        - 如果由于服务器宕机等严重问题，那我们就需要手工进行ACK，保证消费端消费成功
    - 消费端的重回队列
        - 消费端重回队列是为了对没有处理成功的消息，把消息重新回递给Broker
        - 一般我们在实际应用中，都会关闭重回队列，也就是设置为False
- TTL队列/消息
    - TTL是Time To Live的缩写，也就是生存时间
    - RabbitMQ支持消息的过期时间，在消息发送时可以指定
    - RabbitMQ支持队列的过期时间，从消息入队列开始计算，主要超过了队列的超时时间配置，那么消息会自动地清除
- 死信队列（DLX，Dead-Letter-Exxchange）
    - 利用DLX，当消息在一个队列中变成死信（dead message）之后，它能被重新publish到另一个Exchange，这个Exchange就是DLX
    - 消息变成死信有以下几种情况
        - 消息被拒绝（basic.reject/basic.nack）并且requeue=false
        - 消息TTL过期
        - 队列达到最大长度
    - DLX也是一个正常的Exchange，和一般的Exchange没有区别，它能在任何的队列上被指定，实际上就是设置某个队列的属性
    - 当这个队列中有死信时，RabbitMQ就会自动地将这个消息重新发布到设置的Exchange上去，进而被路由到另一个队列
    - 可以监听这个队列中消息做相应的处理
## 第4章 手把手，整合RabbitMQ&Spring家族
## 第5章 高可靠，构建RabbitMQ集群架构
- RabbitMQ集群架构模式
    - 主备模式：实现RabbitMQ的高可用集群，一般在并发和数据量不高的情况下，这种模型非常的好用且简单。主备模式也称之为Warren模式
    - 远程模式：远程模式可以实现双活的一种模式，简称Shovel模式，所谓Shovel就是我们可以把消息进行不同数据中心的复制工作，我们可以跨地域地让两个mq集群互联
    - 镜像模式：集群模式非常经典的就是Mirror镜像模式，保证100%数据不丢失，在实际工作中也是用的最多的。并且实现集群非常的简单，一般互联网大厂都会构建这种镜像集群模式
    - 多活模式：这种模式也是实现异地数据复制的主流模式，因为Shovel模式配置比较复杂，所以一般来说实现异地集群都是使用这种双活或者多活模型来实现的。这种模型需要依赖rabbitmq的federation插件，可以实现持续的可靠的AMQP数据通信，多活模式在实际配置与应用非常的简单
## 第6章 追前沿，领略SET化架构衍化与设计
- BAT/TMD大厂单元化架构设计衍变之路
    - 随着大型互联网公司业务的多元化发展，就拿滴滴、美团等大厂来讲，如滴滴打车、单车、外卖、酒店、旅行、金融等业务持续高速增长，单个大型分布式体系的集群，通过加机器+集群内部拆分（kv、mq、mysql等），虽然具备了一定的可扩展性，但是，随着业务量的进一步增长，整个集群规模逐渐变得巨大，从而一定会在某个点上达到瓶颈，无法满足扩展性需要，并且大集群内核心服务出现问题，会影响全网所有用户
    - 以滴滴打车、美团外卖举例来说：
        - 打车业务体量巨大，尤其是在早晚高峰期。全年订单量已越10亿
        - 外卖业务体量庞大，目前单量已突破1700w/天
    - 对于如此庞大的单个大型分布式集群，会面临以下问题
        - 1 容灾问题
            - 核心服务（比如订单服务）挂掉，会影响全网所有用户，导致整个业务不可用
            - 数据库主库集中在一个IDC，主机房挂掉，会影响全网所有用户，整个业务无法快速切换和恢复
        - 2 资源扩展问题
            - 单IDC的资源（机器、网络带宽等）已经没法满足，扩展IDC时，存在跨机房访问时延问题（增加异地机房时，时延问题更加严重）
            - 数据库主库单点，连接数有限，不能支持应用程序的持续扩展
        - 3 大集群拆分问题
            - 核心问题：分布式集群规模扩大后，会相应的带来资源扩展、大集群拆分以及容灾问题
            - 所以出于对业务扩展性以及容灾需求的考虑，我们需要一套从底层架构彻底解决问题的方案
    - 业界主流解决方案
        - 同城“双活”架构
            - 目前很多大型互联网公司 的业务架构可以理解为同城“双活”架构
                - 业务层面上已经做到真正的双活（或者多活），分别承担部分流量
                - 存储层面比如定时任务、缓存、持久层、数据分析等都是主从架构，会有跨机房写
                - 一个数据中心故障，可以手动切换流量，部分组件可以自动切换
        - 两地三中心架构
            - 使用灾备的思想，在同城“双活”的基础上，在异地部署一套灾备数据中心，每个中心都具有完备的数据处理能力，只有当主节点故障需要容灾的时候才会紧急启动备用数据中心
        - SET化方案目标
            - 业务：解决业务遇到的扩展性和容灾等需求，支撑业务的高速发展
            - 通用性：架构侧形成统一通用的解决方案，方便各业务线接入使用
- SET化架构策略
    - 流量路由
        - 按照特殊的key（通常为userid）进行路由，判断某次请求该路由到中心集群还是单元化集群
    - 中心集群
        - 未进行单元化改造的服务（通常不在核心交易链路，比如供应链系统）称为中心集群，跟当前架构保持一致
    - 单元化集群
        - 每个单元化集群只负责本单元内的流量处理，以实现流量拆分以及故障隔离
        - 每个单元化集群前期只存储本单元产生的交易数据，后续会做双向数据同步，实现容灾切换需求
    - 中间件（RPC、KV、MQ等）
        - RPC：对于SET服务，调用封闭在SET内；对于非SET服务，沿用现有路由逻辑
        - KV：支持分SET的数据生产和查询
        - MQ：支持分SET的消息生产和消费
    - 数据同步
        - 全局数据（数据量小且变化不大，比如商家的菜品数据）部署在中心集群，其他单元化集群同步全局数据到本单元化内
        - 未来演变为异地多活架构时，各单元化集群数据需要进行双向同步来实现容灾需要
    - SET化路由策略及其能力
        - 异地容灾
            - 通过SET化架构的流量调度能力，将SET分别部署在不同地区的数据中心，实现跨地区容灾支持
        - 高效本地化服务
            - 利用前端位置信息采集和域名解析策略，将流量路由到最近的SET，提供最高效的本地化服务
            - 比如O2O场景天然具有本地生产，本地消费的特点，更加需要SET化支持
        - 集装箱式扩展
            - SET的封装性支持更灵活的部署扩展性，比如SET一键创建/下线，SET一键发布等
- SET化架构原则
    - 对业务透明原则
        - SET化架构的实现对业务代码透明，业务代码层面不需要关心SET化规则，SET的部署等问题
    - SET切分规则
        - 理论上，切分规则由业务层面按需定制
        - 实现上，建议优先选最大的业务维度进行切分
        - 比如海量用户的O2O业务，按用户位置信息进行切分。此外，接入层、逻辑层和数据层可以有独立的SET切分规则，有利于实现部署和运维成本的最优化
    - 部署规范原则
        - 一个SET并不一定只限制在一个机房，也可以跨机房或者跨地区部署；为保证灵活性，单个SET内机器数不宜过多（如不超过1000台物理机）
## 第7章 学大厂，拓展基础组件封装思路
- MQ组件实现功能点
    - 支持消息高性能的序列化转换、异步化发送消息
    - 支持消息生产实例与消费实例的链接池化缓存化，提升性能
    - 支持可靠性投递消息，保障消息的100%不丢失
    - 支持消费端的幂等操作，避免消费端重复消费的问题
    - 支持迅速消息发送模式，在一些日志收集/统计分析等需求下可以保证高性能，超高吞吐量
    - 支持延迟消息模式，消息可以延迟发送，指定延迟时间，用于某些延迟检查，服务限流场景
    - 支持事务消息，且100%保障可靠性投递，在金融行业单笔大金额操作时会有此类需求
    - 支持顺序消息，保证消息送达消费端的前后顺序，例如下订单等复合性操作
    - 支持消息补偿，重试，以及快速定位异常/失败消息
    - 支持集群消息负载均衡，保障消息落到具体SET集群的负载均衡
    - 支持消息路由策略，指定某些消息路由到指定的SET集群
- 消息的幂等性的必要性
    - 可能导致消息出现非幂等的原因
        - 1 可靠性消息投递机制
        - 2 MQ Broker服务与消费端传输消息的过程中的网络抖动
        - 3 消费端故障或异常
## 第8章 课程总结