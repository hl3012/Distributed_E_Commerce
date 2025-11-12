import * as cdk from 'aws-cdk-lib';
import * as ec2 from 'aws-cdk-lib/aws-ec2';
import * as ecs from 'aws-cdk-lib/aws-ecs';
import * as ecr from 'aws-cdk-lib/aws-ecr';
import * as elbv2 from 'aws-cdk-lib/aws-elasticloadbalancingv2';
import * as logs from 'aws-cdk-lib/aws-logs';
import { Construct } from 'constructs';

interface EcsStackProps extends cdk.StackProps {
    appName: string;
    stage: string;
    repository: ecr.Repository;
}

export class EcsStack extends cdk.Stack {
    public readonly cluster: ecs.Cluster;
    public readonly service: ecs.FargateService;

    constructor(scope: Construct, id: string, props: EcsStackProps) {
        super(scope, id, props);

        const vpc = new ec2.Vpc(this, 'Vpc', {
            maxAzs: 2,
            natGateways: 1
        });

        this.cluster = new ecs.Cluster(this, 'EcsCluster', {
            vpc,
            clusterName: `${props.appName}-cluster-${props.stage}`
        });

        const taskDefinition = new ecs.FargateTaskDefinition(this, 'TaskDefinition', {
            memoryLimitMiB: 2048,
            cpu: 1024
        });

        const container = taskDefinition.addContainer('SpringBootContainer', {
            image: ecs.ContainerImage.fromEcrRepository(props.repository, 'latest'),
            logging: ecs.LogDriver.awsLogs({
                streamPrefix: props.appName,
                logRetention: logs.RetentionDays.ONE_WEEK
            }),
            portMappings: [{ containerPort: 8080 }],
            environment: {
                SPRING_PROFILES_ACTIVE: props.stage,
                JAVA_OPTS: '-Xmx1024m -Xms512m'
            }
        });

        const loadBalancer = new elbv2.ApplicationLoadBalancer(this, 'LoadBalancer', {
            vpc,
            internetFacing: true
        });

        const listener = loadBalancer.addListener('Listener', { port: 80 });

        this.service = new ecs.FargateService(this, 'FargateService', {
            cluster: this.cluster,
            taskDefinition,
            desiredCount: 2,
            assignPublicIp: true,
            healthCheckGracePeriod: cdk.Duration.seconds(120)
        });

        listener.addTargets('EcsTargets', {
            port: 8080,
            targets: [this.service],
            healthCheck: {
                path: '/actuator/health',
                interval: cdk.Duration.seconds(30),
                timeout: cdk.Duration.seconds(5)
            }
        });

        new cdk.CfnOutput(this, 'LoadBalancerDns', {
            value: loadBalancer.loadBalancerDnsName
        });
    }
}