import * as cdk from 'aws-cdk-lib';
import * as codebuild from 'aws-cdk-lib/aws-codebuild';
import * as codepipeline from 'aws-cdk-lib/aws-codepipeline';
import * as codepipeline_actions from 'aws-cdk-lib/aws-codepipeline-actions';
import * as ecr from 'aws-cdk-lib/aws-ecr';
import * as ecs from 'aws-cdk-lib/aws-ecs';
import * as iam from 'aws-cdk-lib/aws-iam';
import * as s3 from 'aws-cdk-lib/aws-s3';
import { Construct } from 'constructs';

interface PipelineStackProps extends cdk.StackProps {
    appName: string;
    stage: string;
    ecrRepository: ecr.Repository;
    ecsService: ecs.FargateService;
    ecsCluster: ecs.Cluster;
}

export class PipelineStack extends cdk.Stack {
    constructor(scope: Construct, id: string, props: PipelineStackProps) {
        super(scope, id, props);

        const buildProject = new codebuild.Project(this, 'BuildProject', {
            projectName: `${props.appName}-build`,
            environment: {
                buildImage: codebuild.LinuxBuildImage.AMAZON_LINUX_2_3,
                privileged: true,
                environmentVariables: {
                    AWS_ACCOUNT_ID: { value: this.account },
                    IMAGE_REPO_NAME: { value: props.ecrRepository.repositoryName },
                    // IMAGE_TAG: { value: 'latest' },
                    CONTAINER_NAME: { value: 'SpringBootContainer' },
                    AWS_DEFAULT_REGION: { value: this.region }
                }
            },
            buildSpec: codebuild.BuildSpec.fromSourceFilename('buildspec.yml'),
            cache: codebuild.Cache.local(codebuild.LocalCacheMode.DOCKER_LAYER),
            timeout: cdk.Duration.minutes(30)
        });


        props.ecrRepository.grantPullPush(buildProject);

        buildProject.addToRolePolicy(new iam.PolicyStatement({
            actions: [
                'ecs:DescribeServices',
                'ecs:UpdateService',
                'ecr:GetAuthorizationToken'
            ],
            resources: ['*']
        }));

        buildProject.addToRolePolicy(new iam.PolicyStatement({
            actions: [
                'codepipeline:PutJobSuccessResult',
                'codepipeline:PutJobFailureResult'
            ],
            resources: ['*']
        }));

        const artifactBucket = new s3.Bucket(this, 'ArtifactBucket', {
            removalPolicy: cdk.RemovalPolicy.DESTROY,
            autoDeleteObjects: true
        });


        const sourceOutput = new codepipeline.Artifact('SourceOutput');
        const buildOutput = new codepipeline.Artifact('BuildOutput');


        const pipeline = new codepipeline.Pipeline(this, 'Pipeline', {
            pipelineName: `${props.appName}-pipeline`,
            artifactBucket,
            restartExecutionOnUpdate: true
        });


        pipeline.addStage({
            stageName: 'Source',
            actions: [
                new codepipeline_actions.GitHubSourceAction({
                    actionName: 'GitHub_Source',
                    owner: 'hl3012',
                    repo: 'Distributed_E_Commerce',
                    branch: 'master',
                    oauthToken: cdk.SecretValue.secretsManager('github-token'),
                    output: sourceOutput,
                    trigger: codepipeline_actions.GitHubTrigger.WEBHOOK
                })
            ]
        });


        pipeline.addStage({
            stageName: 'Build',
            actions: [
                new codepipeline_actions.CodeBuildAction({
                    actionName: 'Build_And_Push_Image',
                    project: buildProject,
                    input: sourceOutput,
                    outputs: [buildOutput],
                    environmentVariables: {
                        IMAGE_TAG: {
                            value: '#{codepipeline.PipelineExecutionId}'
                        }
                    }
                })
            ]
        });


        pipeline.addStage({
            stageName: 'Deploy',
            actions: [
                new codepipeline_actions.EcsDeployAction({
                    actionName: 'Deploy_to_ECS',
                    service: props.ecsService,
                    imageFile: new codepipeline.ArtifactPath(
                        buildOutput,
                        'imagedefinitions.json'
                    )
                })
            ]
        });


        new cdk.CfnOutput(this, 'PipelineUrl', {
            value: `https://${this.region}.console.aws.amazon.com/codesuite/codepipeline/pipelines/${pipeline.pipelineName}/view`
        });
    }
}